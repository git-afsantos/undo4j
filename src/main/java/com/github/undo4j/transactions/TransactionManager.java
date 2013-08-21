package com.github.undo4j.transactions;

import com.github.undo4j.common.AccessMode;
import com.github.undo4j.common.IsolationLevel;

import java.util.concurrent.Future;

/**
 * TransactionManager
 * 
 * @author afs
 * @version 2013
 */

public interface TransactionManager {
	/** */
	<T> Future<T> submit(TransactionalCallable<T> transaction);

	/** */
	<T> Future<T> submit(TransactionalCallable<T> transaction, AccessMode mode);

	/** */
	<T> Future<T> submit(TransactionalCallable<T> transaction, AccessMode mode, IsolationLevel isolation);

	/** */
	void shutdown();
}
