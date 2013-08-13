package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.IsolationLevel;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * TransactionManager
 * 
 * @author afs
 * @version 2013
*/

public interface TransactionManager extends ResourcePool,
        ResourceHandleProvider {
    /** */
    <T> Future<TransactionResult<T>> submit(Callable<T> transaction);

    /** */
    <T> Future<TransactionResult<T>> submit(
        Callable<T> transaction, AccessMode mode
    );

    /** */
    <T> Future<TransactionResult<T>> submit(
        Callable<T> transaction, AccessMode mode, IsolationLevel isolation
    );

    /** */
    void shutdown();
}
