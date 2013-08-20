package com.github.undo4j.resources;

/**
 * ResourceAcquireException
 * 
 * @author afs
 * @version 2013
*/

public class ResourceAcquireException extends ResourceException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class ResourceAcquireException. */
    public ResourceAcquireException() { super(); }


    /** Parameter constructor of objects of class ResourceAcquireException. */
    public ResourceAcquireException(String message) { super(message); }


    /** Parameter constructor of objects of class ResourceAcquireException. */
    public ResourceAcquireException(String message, Throwable cause) {
        super(message, cause);
    }
}
