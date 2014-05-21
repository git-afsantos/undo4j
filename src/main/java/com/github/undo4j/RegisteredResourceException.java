package com.github.undo4j;


/**
 * RegisteredResourceException
 * 
 * @author afs
 * @version 2013
 */

public final class RegisteredResourceException
        extends RuntimeException {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 1L;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class RegisteredResourceException.
     */
    RegisteredResourceException() { super(); }


    /**
     *  Parameter constructor of class RegisteredResourceException.
     */
    RegisteredResourceException(final String message) { super(message); }


    /**
     *  Parameter constructor of class RegisteredResourceException.
     */
    RegisteredResourceException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }

    /**
     *  Parameter constructor of class RegisteredResourceException.
     */
    RegisteredResourceException(final Throwable cause) { super(cause); }
}
