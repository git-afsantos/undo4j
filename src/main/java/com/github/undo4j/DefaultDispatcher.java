package com.github.undo4j;

import static com.github.undo4j.Check.checkNotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/*
 * Alternate API:
 * void write(WriteOperation op, Object resourceToLock);
 * void write(WriteOperation op, Object r1, Object r2);
 * ...
 * void write(WriteOperation op, Object... resourcesToLock);
 * void write(WriteOperation op, Iterable<Object> resourcesToLock);
 * 
 * Same applies to read operations.
 */

/**
 * Instances of DefaultDispatcher are suited for a single transaction and,
 * thus, should not be used outside the body of the transaction.
 * DefaultDispatcher is a dispatcher of operations from transactions.
 * 
 * @author afs
 * @version 2014-01-20
 */

class DefaultDispatcher implements OperationDispatcher {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** The id of the transaction that uses this dispatcher. */
    protected final TransactionId owner;
    // To be used in deadlock detection.

    /** The access mode to use when acquiring locks. */
    protected final AccessMode mode;

    /** The operation to execute on commit. */
    protected final CommitOperation onCommit;

    /** The stack of write operations to rollback. */
    protected final Deque<WriteOperation> stack = new ArrayDeque<>();

    /** The resource manager. */
    protected final ResourceManager manager;

    /** The lock wait strategy to use. */
    protected final WaitStrategy strategy;

    /** The set of acquired resources. */
    protected final Set<ResourceId> acquired    = new HashSet<>();

    /** The thread running the transaction of this dispatcher. */
    private volatile Thread thread              = null;

    /** Tells whether this manager should interrupt execution. */
    private volatile boolean interrupted        = false;

    /** Tells whether this manager started committing. */
    protected boolean committing                = false;

    /** Tells whether this manager was used. */
    protected boolean used                      = false;

    /** Tells whether this manager can be used. */
    protected boolean alive                     = true;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of class DefaultDispatcher.
     */
    protected DefaultDispatcher(
            final TransactionId     owner,
            final AccessMode        mode,
            final ResourceManager   manager,
            final WaitStrategy      strategy,
            final CommitOperation   onCommit) {
        assert owner != null && mode != null && onCommit != null
            && manager != null && strategy != null;
        this.owner      = owner;
        this.mode       = mode;
        this.onCommit   = onCommit;
        this.manager    = manager;
        this.strategy   = strategy;
    }



    /*************************************************************************\
     *  Predicates
    \*************************************************************************/

    /** */
    protected final boolean isCommitting() {
        return this.committing;
    }

    /** */
    protected final boolean isUsed() {
        return this.used;
    }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    @Override
    public final <T> T read(final ReadOperation<T> op) {
        readDecorator(op);
        acquire(op.resources());
        this.used = true;
        return op.read();
    }


    /** */
    @Override
    public final void write(final WriteOperation op) {
        writeDecorator(op);
        acquire(op.resources());
        this.stack.addLast(op);
        this.used = true;
        op.write();
    }


    /** */
    @Override
    public final void abort() {
        throw new AbortRequestedException("transaction aborted");
    }


    /** */
    @Override
    public final void abort(final String message) {
        throw new AbortRequestedException(message);
    }



    /*************************************************************************\
     *  Protected Methods
    \*************************************************************************/

    /** */
    protected void commit() {
        checkNotInterrupted();
        checkNotEmpty();
        this.committing = true;
        this.onCommit.commit();
        this.stack.clear();
        this.alive = false;
    }


    /** */
    protected void rollback() {
        if (this.committing) {
            this.onCommit.undo();
        }
        for (WriteOperation op = this.stack.pollLast();
                op != null; op = this.stack.pollLast()) {
            op.undo();
        }
        this.stack.clear();
        this.alive = false;
    }


    /** */
    protected final void shutdown() { this.alive = false; }


    /** */
    protected void release() {
        for (ResourceId r: acquired) { manager.release(owner, r); }
        this.acquired.clear();
    }

    /** */
    protected void interrupt() {
        this.interrupted = true;
        if (this.thread != null) { this.thread.interrupt(); }
        shutdown();
    }

    /** */
    protected void bind() { this.thread = Thread.currentThread(); }

    /** */
    protected void unbind() { this.thread = null; }


    /** */
    protected <T> void readDecorator(final ReadOperation<T> op) {
        checkNotNull(op);
        checkAlive();
        checkTransaction();
        checkNotInterrupted();
    }


    /** */
    protected void writeDecorator(final WriteOperation op) {
        checkNotNull(op);
        checkAlive();
        checkWriter();
        checkTransaction();
        checkNotInterrupted();
    }


    /** */
    protected final void checkWriter() {
        if (this.mode == AccessMode.READ) {
            throw new ReadOnlyTransactionException("read-only transaction");
        }
    }

    /** */
    protected final void checkNotEmpty() {
        if (!this.used) {
            throw new EmptyTransactionException("empty transaction");
        }
    }

    /** */
    protected final void checkAlive() {
        if (!this.alive) {
            throw new IllegalStateException("unusable dispatcher");
        }
    }

    /** */
    protected final void checkTransaction() {
        if (Thread.currentThread() != this.thread) {
            throw new TransactionContextException("not a transaction");
        }
    }


    /** */
    protected final void checkNotInterrupted() {
        if (this.thread.isInterrupted() || this.interrupted) {
            throw new TransactionInterruptedException
                ("transaction interrupted");
        }
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /**
     * Acquires locks in order, to avoid deadlocks.
     */
    private void acquire(final Iterable<ResourceId> resources) {
        Queue<ResourceId> rs = sortResources(resources);
        for (ResourceId i = rs.poll(); i != null; i = rs.poll()) {
            if (!this.acquired.contains(i)) {
                manager.acquire(owner, i, mode, strategy);
                this.acquired.add(i);
            }
        }
    }


    /**
     * Sorts non-acquired locks, to avoid deadlocks.
     */
    private Queue<ResourceId> sortResources(final Iterable<ResourceId> rs) {
        boolean isEmpty = true;
        Queue<ResourceId> queue = new PriorityQueue<>();
        for (ResourceId r: rs) {
            checkNotNull(r);
            isEmpty = false;
            queue.add(r);
        }
        if (isEmpty) {
            throw new ResourceAcquisitionException("No resources to acquire");
        }
        return queue;
    }



    /*************************************************************************\
     *  Equals, HashCode, ToString & Clone
    \*************************************************************************/

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return this.stack.toString();
    }
}
