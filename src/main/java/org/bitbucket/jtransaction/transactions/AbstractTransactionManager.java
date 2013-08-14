package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.IsolationLevel;

import java.util.concurrent.Future;

/**
 * AbstractTransactionManager
 * 
 * @author afs
 * @version 2013
*/

abstract class AbstractTransactionManager
        implements TransactionManager, TransactionListener {
    // instance variables
    private final ThreadLocal<Transaction<?>> transactions =
            new ThreadLocal<Transaction<?>>();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class AbstractTransactionManager.
     */
    protected AbstractTransactionManager() {}



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    protected final Transaction<?> getTransaction() {
        return this.transactions.get();
    }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** Sets the current thread as the given transaction. */
    protected final <T> void setTransaction(Transaction<T> t) {
        this.transactions.set(t);
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** Checks whether the current thread is a transaction.
     */
    protected final boolean isTransaction() {
        return this.transactions.get() != null;
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** Submits the transaction of execution.
     * Assumes default access mode: write.
     * Assumes default isolation level: none.
     */
    @Override
    public <T> Future<T> submit(TransactionalCallable<T> task) {
        return submit(task, AccessMode.WRITE, IsolationLevel.NONE);
    }

    /** Submits the transaction for execution.
     * Assumes default isolation level: none.
     */
    @Override
    public <T> Future<T> submit(
        TransactionalCallable<T> task, AccessMode mode
    ) {
        return submit(task, mode, IsolationLevel.NONE);
    }


    /** Registers the current thread as a transaction. */
    @Override
    public <T> void bind(Transaction<T> t) {
        this.transactions.set(t);
    }

    /** Unregisters the current thread as a transaction. */
    @Override
    public void terminate() {
        this.transactions.remove();
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    protected final void checkTransaction() {
        if (!isTransaction()) {
            throw new TransactionContextException("not a transaction");
        }
    }


    /** */
    protected final void checkNotTransaction() {
        if (isTransaction()) {
            throw new TransactionContextException("thread is a transaction");
        }
    }



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        return this.transactions.toString();
    }
}
