package org.bitbucket.jtransaction.transactions;


/**
 * ReadOnlyHandleException
 * 
 * @author afs
 * @version 2013
*/

public final class ReadOnlyControllerException
        extends UnsupportedOperationException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class ReadOnlyHandleException. */
    ReadOnlyControllerException() { super(); }


    /** Parameter constructor of objects of class ReadOnlyHandleException. */
    ReadOnlyControllerException(String msg) { super(msg); }
}
