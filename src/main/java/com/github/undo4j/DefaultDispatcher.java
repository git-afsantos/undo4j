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

    /** The provider of resource locks. */
    protected final LockProvider provider;

    /** The lock acquisition strategy to use. */
    protected final LockStrategy strategy;

    /** The set of acquired locks. */
    protected final Set<Lock> acquired          = new HashSet<>();

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
            final LockProvider      provider,
            final LockStrategy      strategy,
            final CommitOperation   onCommit) {
        assert owner != null && mode != null && onCommit != null
            && provider != null && strategy != null;
        this.owner      = owner;
        this.mode       = mode;
        this.onCommit   = onCommit;
        this.provider   = provider;
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
        for (Lock lock: acquired) { lock.release(); }
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
            throw new TransactionContextException("Not a transaction");
        }
    }


    /** */
    protected final void checkNotInterrupted() {
        if (Thread.interrupted() || this.interrupted) {
            throw new TransactionInterruptedException
                ("Transaction interrutped");
        }
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /**
     * Acquires locks in order, to avoid deadlocks.
     */
    private void acquire(final Iterable<ResourceId> resources) {
        Queue<Lock> locks = getLocks(resources);
        for (Lock lock = locks.poll(); lock != null; lock = locks.poll()) {
            try {
                if (!lock.acquire(mode, strategy)) {
                    throw new ResourceAcquisitionException
                        ("Failed to acquire");
                }
                this.acquired.add(lock);
            } catch (InterruptedException ex) {
                throw new ResourceAcquisitionException("Interrupted");
            }
        }
    }


    /**
     * Sorts non-acquired locks, to avoid deadlocks.
     */
    private Queue<Lock> getLocks(final Iterable<ResourceId> resources) {
        boolean isEmpty = true;
        Queue<Lock> queue = new PriorityQueue<>();
        for (ResourceId resource: resources) {
            checkNotNull(resource);
            isEmpty = false;
            Lock lock = this.provider.getLock(resource);
            if (this.acquired.contains(lock)) { continue; }
            queue.add(lock);
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
