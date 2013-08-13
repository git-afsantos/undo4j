package org.bitbucket.jtransaction.transactions;

import java.util.concurrent.Callable;

/**
 * Transaction
 * 
 * @author afs
 * @version 2013
*/

interface Transaction<T> extends TransactionHeader,
        Callable<TransactionResult<T>>, ReadWriteListener {
    /** */
    void putController(String id, ResourceController controller);

    /** */
    void interrupt();
}
