package com.github.undo4j;


/**
 * ClientException may be thrown from client-provided code to execute
 * in a transactional context.
 * 
 * @author afs
 * @version 2013
 */

public class ClientException extends Exception {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 643218415618152L;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class ClientException.
     */
    public ClientException() { super(); }


    /**
     *  Parameter constructor of class ClientException.
     */
    public ClientException(final String message) { super(message); }


    /**
     *  Parameter constructor of class ClientException.
     */
    public ClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     *  Parameter constructor of class ClientException.
     */
    public ClientException(final Throwable cause) { super(cause); }
}
