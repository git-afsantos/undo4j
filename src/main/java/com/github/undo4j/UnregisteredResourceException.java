package com.github.undo4j;


/**
 * UnregisteredResourceException
 * 
 * @author afs
 * @version 2013
 */

public final class UnregisteredResourceException
        extends RuntimeException {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 1L;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class UnregisteredResourceException.
     */
    UnregisteredResourceException() { super(); }


    /**
     *  Parameter constructor of class UnregisteredResourceException.
     */
    UnregisteredResourceException(final String message) { super(message); }


    /**
     *  Parameter constructor of class UnregisteredResourceException.
     */
    UnregisteredResourceException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }

    /**
     *  Parameter constructor of class UnregisteredResourceException.
     */
    UnregisteredResourceException(final Throwable cause) { super(cause); }
}
