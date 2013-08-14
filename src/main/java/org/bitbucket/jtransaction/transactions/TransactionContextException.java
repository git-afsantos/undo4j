package org.bitbucket.jtransaction.transactions;


/**
 * TransactionContextException
 * 
 * @author afs
 * @version 2013
*/

public final class TransactionContextException extends TransactionException {
    private static final long serialVersionUID = 1L;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class TransactionContextException.
      */
    TransactionContextException() { super(); }


    /** Parameter constructor of objects of class TransactionContextException.
      */
    TransactionContextException(String msg) { super(msg); }
}
