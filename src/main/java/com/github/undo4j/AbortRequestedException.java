package com.github.undo4j;


/**
 * AbortRequestedException
 * 
 * @author afs
 * @version 2013
 */

public final class AbortRequestedException
        extends TransactionRuntimeException {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 1L;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class AbortRequestedException.
     */
    AbortRequestedException() { super(); }


    /**
     *  Parameter constructor of objects of class AbortRequestedException.
     */
    AbortRequestedException(final String message) { super(message); }
}
