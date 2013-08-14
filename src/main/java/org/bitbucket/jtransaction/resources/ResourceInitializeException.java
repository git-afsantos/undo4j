package org.bitbucket.jtransaction.resources;


/**
 * ResourceInitializeException
 * 
 * @author afs
 * @version 2013
*/

public class ResourceInitializeException extends ResourceException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class ResourceInitializeException. */
    public ResourceInitializeException() { super(); }


    /** Parameter constructor of objects of class ResourceInitializeException. */
    public ResourceInitializeException(String message) { super(message); }


    /** Parameter constructor of objects of class ResourceInitializeException. */
    public ResourceInitializeException(String message, Throwable cause) {
        super(message, cause);
    }
}
