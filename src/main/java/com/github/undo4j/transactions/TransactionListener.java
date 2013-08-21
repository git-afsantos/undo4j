package com.github.undo4j.transactions;

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
