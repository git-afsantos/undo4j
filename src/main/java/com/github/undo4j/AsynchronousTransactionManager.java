package com.github.undo4j;

import static com.github.undo4j.Check.checkNotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * AsynchronousTransactionManager
 * 
 * @author afs
 * @version 2013
 */

final class AsynchronousTransactionManager
        extends AbstractConcurrentTransactionManager {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** The executor that performs asynchronous tasks. */
    private final ExecutorService executor;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class AsynchronousTransactionManager.
     */
    AsynchronousTransactionManager(final ExecutorService executor) {
        super();
        assert executor != null;
        this.executor = executor;
    }



    /*************************************************************************\
     *  Predicates
    \*************************************************************************/

    /** */
    @Override
    public boolean isShutdown() {
        return executor.isShutdown();
    }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    @Override
    public void execute(final ClientRunnable runnable,
        final TransactionConfigurations config)
            throws TransactionExecutionException {
        checkNotNull(runnable);
        checkNotNull(config);
        super.checkNotTransaction();
        Transaction<Void> transaction = super.newTransaction(runnable, config);
        Future<Void> future = executor.submit(transaction);
        try {
            try {
                // Wait for completion.
                future.get();
            } catch (InterruptedException ex) {
                // Interrupt and try to wait for completion.
                transaction.interrupt();
                try {
                    future.get();
                } catch (InterruptedException exc) {
                    return;
                }
            }
        } catch (ExecutionException ex) {
            assert ex.getCause() instanceof TransactionExecutionException;
            throw (TransactionExecutionException) ex.getCause();
        }
    }

    /** */
    @Override
    public <T> T execute(final ClientCallable<T> callable,
        final TransactionConfigurations config)
            throws TransactionExecutionException {
        checkNotNull(callable);
        checkNotNull(config);
        super.checkNotTransaction();
        Transaction<T> transaction = super.newTransaction(callable, config);
        Future<T> future = executor.submit(transaction);
        try {
            try {
                // Wait for completion.
                return future.get();
            } catch (InterruptedException ex) {
                // Interrupt and try to wait for completion.
                transaction.interrupt();
                try {
                    return future.get();
                } catch (InterruptedException exc) {
                    return null;
                }
            }
        } catch (ExecutionException ex) {
            assert ex.getCause() instanceof TransactionExecutionException;
            throw (TransactionExecutionException) ex.getCause();
        }
    }


    /** */
    @Override
    public Future<Void> submit(final ClientRunnable runnable,
            final TransactionConfigurations config) {
        checkNotNull(runnable);
        checkNotNull(config);
        super.checkNotTransaction();
        return executor.submit(super.newTransaction(runnable, config));
    }


    /** */
    @Override
    public <T> Future<T> submit(final ClientCallable<T> callable,
            final TransactionConfigurations config) {
        checkNotNull(callable);
        checkNotNull(config);
        super.checkNotTransaction();
        return executor.submit(super.newTransaction(callable, config));
    }


    /** */
    @Override
    public void shutdown() {
        super.checkNotTransaction();
        if (!executor.isShutdown()) {
            executor.shutdown();
            super.transactionPool.interruptAll();
        }
    }
}
