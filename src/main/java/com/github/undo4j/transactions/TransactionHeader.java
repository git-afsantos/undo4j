package com.github.undo4j.transactions;

import com.github.undo4j.common.AccessMode;
import com.github.undo4j.common.Copyable;
import com.github.undo4j.common.IsolationLevel;

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
