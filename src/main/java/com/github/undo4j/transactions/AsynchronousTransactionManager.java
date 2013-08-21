package com.github.undo4j.transactions;

import com.github.undo4j.common.AccessMode;
import com.github.undo4j.common.IsolationLevel;

import static com.github.undo4j.common.Check.checkArgument;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/**
 * AsynchronousTransactionManager
 * 
 * @author afs
 * @version 2013
 */

final class AsynchronousTransactionManager extends AbstractTransactionManager {
	// instance variables
	private final ExecutorService executor;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/**
	 * Parameter constructor of class AsynchronousTransactionManager.
	 */
	protected AsynchronousTransactionManager(ExecutorService exec) {
		executor = exec;
	}

	/**************************************************************************
	 * Public Methods
	 **************************************************************************/

	/**
	 * Executes the transaction asynchronously, when possible, with the given
	 * access mode and isolation level.
	 */
	@Override
	public <T> Future<T> submit(TransactionalCallable<T> task, AccessMode mode, IsolationLevel isolation) {
		checkArgument(task);
		checkArgument(mode);
		checkArgument(isolation);
		checkNotTransaction();
		checkNotShutdown();
		return executor.submit(new SimpleTransaction<T>(mode, isolation, this, task));
	}

	/** */
	@Override
	public void shutdown() {
		executor.shutdown();
	}

	/**************************************************************************
	 * Private Methods
	 **************************************************************************/

	/** */
	private void checkNotShutdown() {
		if (executor.isShutdown()) {
			throw new RejectedExecutionException("manager is shut down");
		}
	}
}
