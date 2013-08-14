package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.IsolationLevel;

import static org.bitbucket.jtransaction.common.Check.checkArgument;

import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/**
 * SynchronousTransactionManager
 * 
 * @author afs
 * @version 2013
*/

final class SynchronousTransactionManager extends AbstractTransactionManager {
    // instance variables
    private volatile boolean isShutdown = false;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of class SynchronousTransactionManager. */
    SynchronousTransactionManager() {}



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /**
     * Executes the transaction synchronously, with the given access mode
     * and isolation level.
     */
    @Override
    public <T> Future<T> submit(
        TransactionalCallable<T> task,
        AccessMode mode,
        IsolationLevel isolation
    ) {
        checkArgument(task);
        checkArgument(mode);
        checkArgument(isolation);
        checkNotTransaction();
        checkNotShutdown();
        Transaction<T> transaction = new SimpleTransaction<T>(
            mode, isolation, this, task
        );
        try {
            return new SyncTransactionFuture<T>(transaction.call(), null);
        } catch (Exception e) {
            return new SyncTransactionFuture<T>(null, e);
        }
    }

    /** */
    @Override
    public void shutdown() { isShutdown = true; }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    private void checkNotShutdown() {
        if (isShutdown) {
            throw new RejectedExecutionException("manager is shut down");
        }
    }
}
