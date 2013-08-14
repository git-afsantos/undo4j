package org.bitbucket.jtransaction.transactions;


/**
 * KeyUnregisteredException
 * 
 * @author afs
 * @version 2013
*/

public final class KeyUnregisteredException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class KeyUnregisteredException. */
    KeyUnregisteredException() { super(); }


    /** Parameter constructor of objects of class KeyUnregisteredException. */
    KeyUnregisteredException(String msg) { super(msg); }
}
