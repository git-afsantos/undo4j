package com.github.undo4j;


/**
 * TransactionInterruptedException
 * 
 * @author afs
 * @version 2013
 */

public final class TransactionInterruptedException
        extends TransactionRuntimeException {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 1L;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class TransactionInterruptedException.
     */
    TransactionInterruptedException() { super(); }


    /**
     *  Parameter constructor of class TransactionInterruptedException.
     */
    TransactionInterruptedException(final String message) { super(message); }


    /**
     *  Parameter constructor of class TransactionInterruptedException.
     */
    TransactionInterruptedException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }

    /**
     *  Parameter constructor of class TransactionInterruptedException.
     */
    TransactionInterruptedException(final Throwable cause) { super(cause); }
}
