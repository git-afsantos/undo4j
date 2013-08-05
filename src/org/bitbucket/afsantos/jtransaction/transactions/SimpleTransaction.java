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


package org.bitbucket.afsantos.jtransaction.transactions;

import org.bitbucket.afsantos.jtransaction.common.AccessMode;
import org.bitbucket.afsantos.jtransaction.common.IsolationLevel;

import org.bitbucket.afsantos.jtransaction.resources.ResourceCommitException;
import org.bitbucket.afsantos.jtransaction.resources.ResourceUpdateException;

import java.util.concurrent.Callable;

/**
 * SimpleTransaction
 * 
 * @author afs
 * @version 2013
*/

final class SimpleTransaction<T> extends AbstractTransaction<T> {
    private static final String EMPTY = "empty transaction";

    // instance variables
    private final Callable<T> client;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class SimpleTransaction. */
    SimpleTransaction(
            AccessMode mode,
            IsolationLevel isolation,
            TransactionListener listener,
            Callable<T> body
    ) {
        super(mode, isolation, listener);
        client = body;
    }


    /** Copy constructor of objects of class SimpleTransaction. */
    private SimpleTransaction(SimpleTransaction<T> instance) {
        super(instance);
        client = instance.getClientCallable();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    private Callable<T> getClientCallable() { return client; }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */



    /**************************************************************************
     * Predicates
    **************************************************************************/

    // ...



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public TransactionResult<T> call() throws Exception {
        // Transaction setup --------------------------------------------------
        getListener().bind(this);
        // Transaction body ---------------------------------------------------
        TransactionResult<T> result = computeResult();
        // Transaction cleanup ------------------------------------------------
        // Cleanup - Assure transaction is not empty.
        checkNotEmpty();
        // Cleanup - Try to commit.
        trySafeCommit();
        // Return computed result.
        return result;
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    private TransactionResult<T> computeResult() throws Exception {
        try {
            // Client code ----------------------------------------------------
            return new TransactionResult<T>(client.call(), getStatistics());
        } catch (Exception ex) {
            // Cleanup - Try to roll back.
            rollbackAndRelease();
            // Rethrow exception.
            throw ex;
        }
    }


    /** */
    private void trySafeCommit() {
        try { commit(); }
        catch (Exception ex) { rollback(); }
        finally { releaseHandles(); }
    }



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public SimpleTransaction<T> clone() {
        return new SimpleTransaction<T>(this);
    }
}
