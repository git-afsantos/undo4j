package com.github.undo4j;


/**
 * DefaultTransactionRegistry
 * 
 * @author afs
 * @version 2013
 */

final class DefaultTransactionRegistry implements TransactionRegistry {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** */
    private TransactionId transactionId;

    /** */
    @SuppressWarnings("unused")
    private Transaction<?> transaction;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class DefaultTransactionRegistry.
     */
    DefaultTransactionRegistry() {}



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    @Override
    public TransactionId current() { return transactionId; }



    /*************************************************************************\
     *  Setters
    \*************************************************************************/

    /** */
    void setTransaction(final Transaction<?> transaction) {
        assert transaction != null;
        this.transaction = transaction;
    }



    /*************************************************************************\
     *  Predicates
    \*************************************************************************/

    /** */
    @Override
    public boolean isTransaction() { return transactionId != null; }

    /** */
    @Override
    public boolean isTransaction(final TransactionId id) {
        assert id != null;
        return id.equals(transactionId);
    }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    public void bind(final TransactionId id) {
        assert id != null;
        transactionId = id;
    }

    /** */
    @Override
    public void unbind() {
        transactionId = null;
        transaction = null;
    }
}
