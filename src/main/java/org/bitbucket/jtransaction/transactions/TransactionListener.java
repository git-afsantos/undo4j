package org.bitbucket.jtransaction.transactions;


/**
 * TerminationListener
 * 
 * @author afs
 * @version 2013
*/

interface TransactionListener {
    <T> void bind(Transaction<T> transaction);
    void terminate();
}
