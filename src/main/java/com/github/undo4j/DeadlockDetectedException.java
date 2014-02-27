package com.github.undo4j;


/**
 * DeadlockDetectedException
 * 
 * @author afs
 * @version 2013
 */

public final class DeadlockDetectedException
        extends TransactionRuntimeException {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 1L;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class DeadlockDetectedException.
     */
    DeadlockDetectedException() { super(); }


    /**
     *  Parameter constructor of class DeadlockDetectedException.
     */
    DeadlockDetectedException(final String message) { super(message); }


    /**
     *  Parameter constructor of class DeadlockDetectedException.
     */
    DeadlockDetectedException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }

    /**
     *  Parameter constructor of class DeadlockDetectedException.
     */
    DeadlockDetectedException(final Throwable cause) { super(cause); }
}
