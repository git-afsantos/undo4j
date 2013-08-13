package org.bitbucket.jtransaction.resources;


/**
 * ResourceDisposeException
 * 
 * @author afs
 * @version 2013
*/

public class ResourceDisposeException extends ResourceException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class ResourceDisposeException. */
    public ResourceDisposeException() { super(); }


    /** Parameter constructor of objects of class ResourceDisposeException. */
    public ResourceDisposeException(String message) { super(message); }


    /** Parameter constructor of objects of class ResourceDisposeException. */
    public ResourceDisposeException(String message, Throwable cause) {
        super(message, cause);
    }
}
