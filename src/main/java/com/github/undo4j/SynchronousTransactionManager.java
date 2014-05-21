package com.github.undo4j;

import static com.github.undo4j.Check.checkNotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * SynchronousTransactionManager
 * 
 * @author afs
 * @version 2013
 */

final class SynchronousTransactionManager
        extends AbstractConcurrentTransactionManager {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** Tells whether this manager has been shut down. */
    private boolean isShutdown = false;

    /** Internal lock. */
    private final Object internalLock = new Object();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class SynchronousTransactionManager.
     */
    SynchronousTransactionManager() { super(); }



    /*************************************************************************\
     *  Predicates
    \*************************************************************************/

    /** */
    @Override
    public boolean isShutdown() {
        synchronized (internalLock) {
            return isShutdown;
        }
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
        checkNotShutdown();
        super.checkNotTransaction();
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
        super.checkNotTransaction();
        return super.newTransaction(callable, config).call();
    }


    /** */
    @Override
    public Future<Void> submit(final ClientRunnable runnable,
            final TransactionConfigurations config) {
        try {
            this.execute(runnable, config);
            return new SyncFuture<Void>(null, null);
        } catch (TransactionExecutionException ex) {
            return new SyncFuture<Void>(null, ex);
        }
    }


    /** */
    @Override
    public <T> Future<T> submit(final ClientCallable<T> callable,
            final TransactionConfigurations config) {
        try {
            return new SyncFuture<T>(this.execute(callable, config), null);
        } catch (TransactionExecutionException ex) {
            return new SyncFuture<T>(null, ex);
        }
    }


    /** */
    @Override
    public void shutdown() {
        super.checkNotTransaction();
        boolean interrupt = false;
        synchronized (internalLock) {
            if (!isShutdown) {
                interrupt = isShutdown = true;
            }
        }
        if (interrupt) {
            super.transactionPool.interruptAll();
        }
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */
    private void checkNotShutdown() {
        if (isShutdown) {
            throw new RejectedExecutionException("Manager is shut down");
        }
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /** */
    private static final class SyncFuture<T> implements Future<T> {
        final T result;
        final TransactionExecutionException exception;

        /** Parameter constructor. */
        SyncFuture(final T result,
                final TransactionExecutionException exception) {
            this.result     = result;
            this.exception  = exception;
        }


        /** */
        @Override
        public T get() throws ExecutionException {
            if (exception == null) {
                return result;
            }
            throw new ExecutionException(exception);
        }

        /** */
        @Override
        public T get(final long timeout, final TimeUnit unit)
                throws ExecutionException {
            return this.get();
        }


        /** */
        @Override
        public boolean isDone() { return true; }

        /** */
        @Override
        public boolean isCancelled() { return false; }


        /** */
        @Override
        public boolean cancel(final boolean mayInterrupt) { return false; }
    }
}
