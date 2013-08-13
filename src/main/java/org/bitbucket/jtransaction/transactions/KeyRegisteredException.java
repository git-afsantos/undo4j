package org.bitbucket.jtransaction.transactions;


/**
 * KeyRegisteredException
 * 
 * @author afs
 * @version 2013
*/

public final class KeyRegisteredException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class KeyRegisteredException. */
    KeyRegisteredException() { super(); }


    /** Parameter constructor of objects of class KeyRegisteredException. */
    KeyRegisteredException(String msg) { super(msg); }
}
