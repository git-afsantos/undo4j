package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.IsolationLevel;

import org.bitbucket.jtransaction.resources.ResourceCommitException;
import org.bitbucket.jtransaction.resources.ResourceUpdateException;

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
