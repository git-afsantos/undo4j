/*
The MIT License (MIT)

Copyright (c) 2013 Andre Santos, Victor Miraldo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/


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
    public static TransactionManager newSingleThreadSyncManager() {
        return new SynchronousTransactionManager(new ResourceManager());
    }

    /** */
    public static TransactionManager newConcurrentSyncManager() {
        return new SynchronousTransactionManager(
            new ConcurrentResourceManager()
        );
    }

    /** */
    public static TransactionManager newFixedThreadPool(int maxThreads) {
        ExecutorService exec = Executors.newFixedThreadPool(maxThreads);
        return new AsynchronousTransactionManager(
            new ConcurrentResourceManager(), exec
        );
    }

    /** */
    public static TransactionManager newSingleThreadAsyncManager() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        return new AsynchronousTransactionManager(
            new ConcurrentResourceManager(), exec
        );
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
}
