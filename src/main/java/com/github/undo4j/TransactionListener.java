package com.github.undo4j;


/**
 * Useful to track when a transaction started,
 * committed or rolled back.
 * 
 * @author afs
 * @version 2013
 */

public interface TransactionListener {
    void started();
    void committed();
    void rolledBack();
}
