/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Andre Santos, Victor Miraldo
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.bitbucket.jtransaction.transactions;

import static org.bitbucket.jtransaction.common.Check.checkArgument;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.IsolationLevel;

/**
 * SingleThreadSyncManager
 * 
 * @author afs
 * @version 2013
*/

final class SingleThreadSyncManager extends AbstractTransactionManager {
    // instance variables
    private boolean isShutdown = false;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class SingleThreadSyncManager. */
    SingleThreadSyncManager(ResourceManager rm) {
        super(rm);
    }

    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /**
     * Executes the transaction synchronously, with the given access mode
     * and isolation level.
     */
    public <T> Future<TransactionResult<T>> submit(Callable<T> task, AccessMode mode, IsolationLevel isolation) {
        checkArgument(task);
        checkArgument(mode);
        checkArgument(isolation);
        checkNotTransaction();
        checkNotShutdown();
        Transaction<T> transaction = new SimpleTransaction<T>(mode, isolation, this, task);
        try {
            return new SyncTransactionFuture<T>(transaction.call(), null);
        } catch (Exception e) {
            return new SyncTransactionFuture<T>(null, e);
        }
    }

    /** */
    public void shutdown() {
        isShutdown = true;
    }

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
