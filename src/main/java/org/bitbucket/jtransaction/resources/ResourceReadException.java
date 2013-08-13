package org.bitbucket.jtransaction.resources;


/**
 * ResourceReadException
 * 
 * @author afs
 * @version 2013
*/

public class ResourceReadException extends ResourceException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class ResourceReadException. */
    public ResourceReadException() { super(); }


    /** Parameter constructor of objects of class ResourceReadException. */
    public ResourceReadException(String message) { super(message); }


    /** Parameter constructor of objects of class ResourceReadException. */
    public ResourceReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
