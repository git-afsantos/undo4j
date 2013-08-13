package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.Copyable;
import org.bitbucket.jtransaction.common.IsolationLevel;

/**
 * Transaction
 * 
 * @author afs
 * @version 2013
*/

public interface TransactionHeader extends Copyable<TransactionHeader> {
    /** */
    TransactionId getId();

    /** */
    AccessMode getAccessMode();

    /** */
    IsolationLevel getIsolationLevel();

    /** */
    boolean isReader();

    /** */
    boolean isWriter();
}
