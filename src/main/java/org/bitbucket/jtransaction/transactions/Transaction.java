package org.bitbucket.jtransaction.transactions;

import java.util.concurrent.Callable;

/**
 * Transaction
 * 
 * @author afs
 * @version 2013
*/

interface Transaction<T> extends TransactionHeader, Callable<T> {
    /** */
    void interrupt();
}