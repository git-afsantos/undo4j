package com.github.undo4j;

import static com.github.undo4j.Check.checkNotNull;
import static com.github.undo4j.Check.checkNoNulls;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AbstractConcurrentTransactionManager
 * 
 * @author afs
 * @version 2013
 */

abstract class AbstractConcurrentTransactionManager
    extends AbstractTransactionManager
        implements ConcurrentTransactionManager {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** Internal transaction pool. */
    protected final TransactionPool transactionPool = new TransactionPool();

    /** Atomic integer containing the next Transaction ID to be assigned. */
    private final AtomicInteger transactionIds = new AtomicInteger();

    /** Atomic integer containing the next Resource ID to be assigned. */
    private final AtomicInteger resourceIds = new AtomicInteger();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class AbstractConcurrentTransactionManager.
     */
    protected AbstractConcurrentTransactionManager() { super(); }



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    @Override
    protected final TransactionPool getTransactionRegistry() {
        return this.transactionPool;
    }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    @Override
    public final <V> ResourceReference<V> registerShareable
            (final V resource) {
        checkNotNull(resource);
        checkNotTransaction();
        ResourceReference<V> ref = super.newResource(resource);
        this.lockPool.putReadWrite(ref.id());
        return ref;
    }

    /** */
    @Override
    public final <V> List<ResourceReference<V>> registerAllShareable
            (final Iterable<V> resources) {
        checkNoNulls(resources);
        checkNotTransaction();
        List<ResourceReference<V>> list = new ArrayList<>();
        for (V r: resources) {
            ResourceReference<V> ref = super.newResource(r);
            this.lockPool.putReadWrite(ref.id());
            list.add(ref);
        }
        return list;
    }


    /** By default: exclusive */
    @Override
    public final <V> ResourceReference<V> register(final V resource) {
        checkNotNull(resource);
        checkNotTransaction();
        ResourceReference<V> ref = super.newResource(resource);
        this.lockPool.putExclusive(ref.id());
        return ref;
    }

    /** By default: exclusive */
    @Override
    public final <V> List<ResourceReference<V>> registerAll
            (final Iterable<V> resources) {
        checkNoNulls(resources);
        checkNotTransaction();
        List<ResourceReference<V>> list = new ArrayList<>();
        for (V r: resources) {
            ResourceReference<V> ref = super.newResource(r);
            this.lockPool.putExclusive(ref.id());
            list.add(ref);
        }
        return list;
    }


    /** */
    @Override
    public final Future<Void> submit(final ClientRunnable runnable) {
        return this.submit(runnable, TransactionConfigurations.defaults());
    }


    /** */
    @Override
    public final <T> Future<T> submit(final ClientCallable<T> callable) {
        return this.submit(callable, TransactionConfigurations.defaults());
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */
    @Override
    protected final TransactionId nextTransactionId() {
        return new TransactionId(this.id, transactionIds.getAndIncrement());
    }

    /** */
    @Override
    protected final ResourceId nextResourceId() {
        return new ResourceId(this.id, resourceIds.getAndIncrement());
    }


    /** */
    @Override
    protected final ConcurrentLockPool newLockPool() {
        return new ConcurrentLockPool();
    }


    /** */
    @Override
    protected final <T> void registerTransaction(
            final Transaction<T> transaction) {
        this.transactionPool.put(transaction.getId(), transaction);
    }


    /** */
    @Override
    protected final void checkNotTransaction() {
        if (this.transactionPool.isTransaction()) {
            throw new TransactionContextException
                ("Thread is running in transactional context");
        }
    }
}
