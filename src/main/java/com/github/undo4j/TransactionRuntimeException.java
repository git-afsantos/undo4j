package com.github.undo4j;


/**
 * TransactionRuntimeException
 * 
 * @author afs
 * @version 2013
 */

public abstract class TransactionRuntimeException extends RuntimeException {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 1L;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     * Empty constructor of objects of class TransactionRuntimeException.
     */
    protected TransactionRuntimeException() { super(); }

    /**
     * Parameter constructor of objects of class TransactionRuntimeException.
     */
    protected TransactionRuntimeException(final String message) {
        super(message);
    }

    /**
     * Parameter constructor of objects of class TransactionRuntimeException.
     */
    protected TransactionRuntimeException(final Throwable cause) {
        super(cause);
    }

    /**
     * Parameter constructor of objects of class TransactionRuntimeException.
     */
    protected TransactionRuntimeException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
