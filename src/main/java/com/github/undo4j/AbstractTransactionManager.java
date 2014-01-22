package com.github.undo4j;

import static com.github.undo4j.Check.checkNoNulls;
import static com.github.undo4j.Check.checkNotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AbstractTransactionManager
 * 
 * @author afs
 * @version 2013
 */

abstract class AbstractTransactionManager implements TransactionManager {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    /** Atomic integer containing the next manager ID to be assigned. */
    private static final AtomicInteger NEXT_ID = new AtomicInteger();



    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** Unique manager id. */
    protected final int id = NEXT_ID.incrementAndGet();

    /** Internal lock pool. */
    protected final LockPool lockPool;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class AbstractTransactionManager.
     */
    protected AbstractTransactionManager() {
        lockPool = newLockPool();
    }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    @Override
    public final void unregister(final ResourceId resource) {
        checkNotNull(resource);
        checkNotTransaction();
        this.lockPool.remove(resource);
    }

    /** */
    @Override
    public final void unregisterAll(final Iterable<ResourceId> resources) {
        checkNoNulls(resources);
        checkNotTransaction();
        this.lockPool.remove(resources);
    }


    /** */
    @Override
    public final void execute(final ClientRunnable runnable)
            throws TransactionExecutionException {
        this.execute(runnable, TransactionConfigurations.defaults());
    }


    /** */
    @Override
    public final <T> T execute(final ClientCallable<T> callable)
            throws TransactionExecutionException {
        return this.execute(callable, TransactionConfigurations.defaults());
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /**
     * Creates and registers a new transaction.
     */
    protected final <T> Transaction<T> newTransaction(
            final ClientCallable<T> client,
            final TransactionConfigurations config) {
        assert client != null && config != null;
        // Create a new transaction with a fresh id and dispatcher.
        final TransactionId tid                 = nextTransactionId();
        final DefaultDispatcher dispatcher      = newDispatcher(tid, config);
        final Transaction<T> transaction        = new Transaction<T>
            (tid, client, dispatcher, this.getTransactionRegistry(),
                config.getListeners());
        // Register transaction.
        this.registerTransaction(transaction);
        return transaction;
    }


    /**
     * Creates and registers a new transaction.
     */
    protected final Transaction<Void> newTransaction(
            final ClientRunnable client,
            final TransactionConfigurations config) {
        return newTransaction
            (new WrappedClientRunnable(client), config);
    }


    /** */
    protected final DefaultDispatcher newDispatcher(
            final TransactionId tid,
            final TransactionConfigurations config) {
        assert tid != null && config != null;
        return new DefaultDispatcher(tid, config.getAccessMode(),
            this.lockPool, valueOfLockStrategy(config),
            config.getCommitOperation());
    }


    /** */
    protected final <V> ResourceReference<V> newResource(final V resource) {
        return new ResourceReference<V>(nextResourceId(), resource);
    }


    /** */
    protected abstract TransactionRegistry getTransactionRegistry();

    /** */
    protected abstract TransactionId nextTransactionId();

    /** */
    protected abstract ResourceId nextResourceId();

    /** */
    protected abstract LockPool newLockPool();

    /** */
    protected abstract <T> void registerTransaction
        (Transaction<T> transaction);

    /** */
    protected abstract void checkNotTransaction();


    /** */
    private LockStrategy valueOfLockStrategy(
            final TransactionConfigurations config) {
        AcquireStrategy strategy = config.getAcquireStrategy();
        if (strategy == AcquireStrategy.TIMED) {
            return LockStrategies.newTimedStrategy
                (config.getTimeoutDelay(), config.getTimeUnit());
        }
        return LockStrategies.newStrategy(strategy);
    }



    /*************************************************************************\
     *  Equals, HashCode, ToString & Clone
    \*************************************************************************/

    /**
     *  Equivalence relation.
     *  Contract (for any non-null reference values x, y, and z):
     *      Reflexive: x.equals(x).
     *      Symmetric: x.equals(y) iff y.equals(x).
     *      Transitive: if x.equals(y) and y.equals(z), then x.equals(z).
     *      Consistency: successive calls return the same result,
     *          assuming no modification of the equality fields.
     *      x.equals(null) should return false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof AbstractTransactionManager)) { return false; }
        AbstractTransactionManager n = (AbstractTransactionManager) o;
        return this.id == n.id;
    }

    /**
     *  Contract:
     *      Consistency: successive calls return the same code,
     *          assuming no modification of the equality fields.
     *      Function: two equal objects have the same (unique) hash code.
     *      (Optional) Injection: unequal objects have different hash codes.
     *
     *  Common practices:
     *      boolean: calculate (f ? 0 : 1);
     *      byte, char, short or int: calculate (int) f;
     *      long: calculate (int) (f ^ (f >>> 32));
     *      float: calculate Float.floatToIntBits(f);
     *      double: calculate Double.doubleToLongBits(f)
     *          and handle the return value like every long value;
     *      Object: use (f == null ? 0 : f.hashCode());
     *      Array: recursion and combine the values.
     *
     *  Formula:
     *      hash = prime * hash + codeForField
     */
    @Override
    public final int hashCode() {
        return this.id;
    }

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return Integer.toString(this.id);
    }
}
