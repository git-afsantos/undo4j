package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.IsolationLevel;

import java.util.concurrent.Future;

/**
 * TransactionManager
 * 
 * @author afs
 * @version 2013
*/

public interface TransactionManager {
    /** */
    <T> Future<T> submit(TransactionalCallable<T> transaction);

    /** */
    <T> Future<T> submit(
        TransactionalCallable<T> transaction,
        AccessMode mode
    );

    /** */
    <T> Future<T> submit(
        TransactionalCallable<T> transaction,
        AccessMode mode,
        IsolationLevel isolation
    );

    /** */
    void shutdown();
}
