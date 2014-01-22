package com.github.undo4j;


/**
 * TransactionRegistry
 * 
 * @author afs
 * @version 2013
 */

interface TransactionRegistry {
    /** */
    TransactionId current();

    /** */
    boolean isTransaction();

    /** */
    boolean isTransaction(final TransactionId id);

    /** */
    void bind(final TransactionId id);

    /** */
    void unbind();
}
