package org.bitbucket.jtransaction.transactions;


/**
 * ReadOnlyHandleException
 * 
 * @author afs
 * @version 2013
*/

public final class ReadOnlyHandleException
        extends UnsupportedOperationException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class ReadOnlyHandleException. */
    ReadOnlyHandleException() { super(); }


    /** Parameter constructor of objects of class ReadOnlyHandleException. */
    ReadOnlyHandleException(String msg) { super(msg); }
}
