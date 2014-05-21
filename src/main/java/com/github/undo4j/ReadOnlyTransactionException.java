package com.github.undo4j;


/**
 * ReadOnlyTransactionException
 * 
 * @author afs
 * @version 2013
 */

public final class ReadOnlyTransactionException
        extends TransactionRuntimeException {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 1L;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class ReadOnlyTransactionException.
     */
    ReadOnlyTransactionException() { super(); }


    /**
     *  Parameter constructor of class ReadOnlyTransactionException.
     */
    ReadOnlyTransactionException(final String message) { super(message); }


    /**
     *  Parameter constructor of class ReadOnlyTransactionException.
     */
    ReadOnlyTransactionException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }

    /**
     *  Parameter constructor of class ReadOnlyTransactionException.
     */
    ReadOnlyTransactionException(final Throwable cause) { super(cause); }
}
