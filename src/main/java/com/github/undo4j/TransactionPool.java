package com.github.undo4j;

import java.util.HashMap;
import java.util.Map;

/**
 * TransactionPool
 * 
 * @author afs
 * @version 2013
 */

final class TransactionPool implements TransactionRegistry {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** */
    private final ThreadLocal<TransactionId> transactions =
        new ThreadLocal<>();

    /** */
    private final Map<TransactionId, Transaction<?>> pool = new HashMap<>();

    /** */
    private final Object internalLock = new Object();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class TransactionPool.
     */
    TransactionPool() {}



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    @Override
    public TransactionId current() { return transactions.get(); }



    /*************************************************************************\
     *  Predicates
    \*************************************************************************/

    /** */
    @Override
    public boolean isTransaction() { return transactions.get() != null; }

    /** */
    @Override
    public boolean isTransaction(final TransactionId id) {
        assert id != null;
        return id.equals(transactions.get());
    }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    public void bind(final TransactionId id) {
        assert id != null;
        transactions.set(id);
    }

    /** */
    @Override
    public void unbind() {
        TransactionId id = transactions.get();
        transactions.remove();
        if (id != null) {
            synchronized (internalLock) {
                pool.remove(id);
            }
        }
    }


    /** */
    <T> void put(final TransactionId id, final Transaction<T> transaction) {
        assert id != null && transaction != null;
        synchronized (internalLock) {
            pool.put(id, transaction);
        }
    }


    /** */
    void interruptAll() {
        synchronized (internalLock) {
            for (Transaction<?> t: pool.values()) {
                t.interrupt();
            }
        }
    }
}
