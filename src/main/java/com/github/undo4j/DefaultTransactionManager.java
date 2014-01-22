package com.github.undo4j;

import static com.github.undo4j.Check.checkNoNulls;
import static com.github.undo4j.Check.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;


/**
 * DefaultTransactionManager
 * 
 * @author afs
 * @version 2013
 */

final class DefaultTransactionManager extends AbstractTransactionManager {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** Single-threaded transaction registry. */
    private final DefaultTransactionRegistry registry =
        new DefaultTransactionRegistry();

    /** Tells whether this manager has been shut down. */
    private boolean isShutdown = false;

    /** Transaction ID generator. */
    private int nextTransactionId = 0;

    /** Resource ID generator. */
    private int nextResourceId = 0;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class DefaultTransactionManager.
     */
    protected DefaultTransactionManager() { super(); }



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    @Override
    protected DefaultTransactionRegistry getTransactionRegistry() {
        return registry;
    }



    /*************************************************************************\
     *  Predicates
    \*************************************************************************/

    /** */
    @Override
    public boolean isShutdown() {
        return isShutdown;
    }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    @Override
    public <V> ResourceReference<V> register(final V resource) {
        checkNotNull(resource);
        checkNotTransaction();
        ResourceReference<V> ref = super.newResource(resource);
        lockPool.putNoLock(ref.id());
        return ref;
    }

    /** */
    @Override
    public <V> List<ResourceReference<V>> registerAll
            (final Iterable<V> resources) {
        checkNoNulls(resources);
        checkNotTransaction();
        List<ResourceReference<V>> list = new ArrayList<>();
        for (V r: resources) {
            ResourceReference<V> ref = super.newResource(r);
            lockPool.putNoLock(ref.id());
            list.add(ref);
        }
        return list;
    }


    /** */
    @Override
    public void execute(final ClientRunnable runnable,
        final TransactionConfigurations config)
            throws TransactionExecutionException {
        checkNotNull(runnable);
        checkNotNull(config);
        checkNotShutdown();
        checkNotTransaction();
        super.newTransaction(runnable, config).call();
    }

    /** */
    @Override
    public <T> T execute(final ClientCallable<T> callable,
        final TransactionConfigurations config)
            throws TransactionExecutionException {
        checkNotNull(callable);
        checkNotNull(config);
        checkNotShutdown();
        checkNotTransaction();
        return super.newTransaction(callable, config).call();
    }


    /** */
    @Override
    public void shutdown() {
        checkNotTransaction();
        isShutdown = true;
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */
    @Override
    protected TransactionId nextTransactionId() {
        return new TransactionId(this.id, nextTransactionId++);
    }

    /** */
    @Override
    protected ResourceId nextResourceId() {
        return new ResourceId(this.id, nextResourceId++);
    }


    /** */
    @Override
    protected LockPool newLockPool() {
        return new LockPool();
    }


    /** */
    @Override
    protected <T> void registerTransaction(final Transaction<T> transaction) {
        assert transaction != null;
        registry.setTransaction(transaction);
    }


    /** */
    @Override
    protected void checkNotTransaction() {
        if (registry.isTransaction()) {
            throw new TransactionContextException
                ("Thread is running in transactional context");
        }
    }


    /** */
    private void checkNotShutdown() {
        if (isShutdown) {
            throw new RejectedExecutionException("Manager is shut down");
        }
    }
}
