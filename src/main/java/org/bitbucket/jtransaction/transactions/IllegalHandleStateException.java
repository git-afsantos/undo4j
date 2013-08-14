package org.bitbucket.jtransaction.transactions;


/**
 * IllegalHandleStateException
 * 
 * @author afs
 * @version 2013
*/

public final class IllegalHandleStateException extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class IllegalHandleStateException. */
    public IllegalHandleStateException() { super(); }

    /** Parameter constructor of objects of class IllegalHandleStateException. */
    public IllegalHandleStateException(String message) {
        super(message);
    }

    /** Parameter constructor of objects of class IllegalHandleStateException. */
    public IllegalHandleStateException(Throwable cause) {
        super(cause);
    }

    /** Parameter constructor of objects of class IllegalHandleStateException. */
    public IllegalHandleStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
