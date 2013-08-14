package org.bitbucket.jtransaction.resources;

/**
 * ResourceInaccessibleException
 * 
 * @author afs
 * @version 2013
*/

public class ResourceInaccessibleException extends ResourceException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class ResourceInaccessibleException. */
    public ResourceInaccessibleException() { super(); }


    /** Parameter constructor of objects of class ResourceInaccessibleException. */
    public ResourceInaccessibleException(String message) { super(message); }


    /** Parameter constructor of objects of class ResourceInaccessibleException. */
    public ResourceInaccessibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
