
package org.bitbucket.jtransaction.transactions;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * TransactionManagers
 * 
 * @author afs
 * @version 2013
*/

public final class TransactionManagers {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class TransactionManagers. */
    public TransactionManagers() {
        throw new UnsupportedOperationException();
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    public static TransactionManager newSynchronousManager() {
        return new SynchronousTransactionManager();
    }

    /** */
    public static TransactionManager newFixedThreadPool(int maxThreads) {
        ExecutorService exec = Executors.newFixedThreadPool(maxThreads);
        return new AsynchronousTransactionManager(exec);
    }

    /** */
    public static TransactionManager newSingleThreadAsyncManager() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        return new AsynchronousTransactionManager(exec);
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
}
