package com.github.undo4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


/**
 * TransactionManagers
 * 
 * @author afs
 * @version 2013
 */

public final class TransactionManagers {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class TransactionManagers.
     */
    private TransactionManagers() {
        throw new AssertionError();
    }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static TransactionManager newSingleThreadManager() {
        return new DefaultTransactionManager();
    }

    /** */
    public static ConcurrentTransactionManager newSynchronousManager() {
        return new SynchronousTransactionManager();
    }

    /** */
    public static ConcurrentTransactionManager newFixedThreadPool
            (final int maxThreads) {
        ExecutorService exec = Executors.newFixedThreadPool(maxThreads);
        return new AsynchronousTransactionManager(exec);
    }

    /** */
    public static ConcurrentTransactionManager newSingleThreadAsyncManager() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        return new AsynchronousTransactionManager(exec);
    }
}
