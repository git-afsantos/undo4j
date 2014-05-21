package com.github.undo4j;


/**
 * TransactionContextException
 * 
 * @author afs
 * @version 2013
 */

public final class TransactionContextException
        extends TransactionRuntimeException {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 1L;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class TransactionContextException.
     */
    TransactionContextException() { super(); }


    /**
     *  Parameter constructor of objects of class TransactionContextException.
     */
    TransactionContextException(final String message) { super(message); }
}
