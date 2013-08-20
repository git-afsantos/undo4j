package com.github.undo4j.transactions;


/**
 * TransactionException
 * 
 * @author afs
 * @version 2013
*/

public abstract class TransactionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class TransactionException. */
    protected TransactionException() { super(); }


    /** Parameter constructor of objects of class TransactionException. */
    protected TransactionException(String message) { super(message); }


    /** Parameter constructor of objects of class TransactionException. */
    protected TransactionException(Throwable cause) { super(cause); }


    /** Parameter constructor of objects of class TransactionException. */
    protected TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
