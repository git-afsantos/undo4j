package com.github.undo4j;


/**
 * EmptyTransactionException
 * 
 * @author afs
 * @version 2013
 */

public final class EmptyTransactionException
        extends TransactionRuntimeException {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 1L;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class EmptyTransactionException.
     */
    EmptyTransactionException() { super(); }


    /**
     *  Parameter constructor of objects of class EmptyTransactionException.
     */
    EmptyTransactionException(final String message) { super(message); }
}
