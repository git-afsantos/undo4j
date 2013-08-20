package com.github.undo4j.transactions;

import java.util.HashMap;
import java.util.Map;

import com.github.undo4j.common.AccessMode;
import com.github.undo4j.common.IsolationLevel;
import com.github.undo4j.resources.ResourceId;

/**
 * SimpleTransaction
 * 
 * @author afs
 * @version 2013
 */

final class SimpleTransaction<T> extends AbstractTransaction<T> {
	private static final String EMPTY = "empty transaction";

	// instance variables
	private final TransactionalCallable<T> client;
	private final Map<ResourceId, ResourceController> controllers = new HashMap<>();

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Parameter constructor of objects of class SimpleTransaction. */
	SimpleTransaction(AccessMode mode, IsolationLevel isolation,
			TransactionListener listener, TransactionalCallable<T> body) {
		super(mode, isolation, listener);
		client = body;
	}

	/** Copy constructor of objects of class SimpleTransaction. */
	private SimpleTransaction(SimpleTransaction<T> instance) {
		super(instance);
		client = instance.getClientCallable();
	}

	/**************************************************************************
	 * Getters
	 **************************************************************************/

	/** */
	private TransactionalCallable<T> getClientCallable() {
		return client;
	}

	/**************************************************************************
	 * Setters
	 **************************************************************************/

	/** */

	/**************************************************************************
	 * Predicates
	 **************************************************************************/

	/** */
	private boolean isEmpty() {
		for (ResourceController rc : controllers.values()) {
			if (rc.isAccessed()) {
				return false;
			}
		}
		return true;
	}

	/**************************************************************************
	 * Public Methods
	 **************************************************************************/

	/** */
	@Override
	public T call() throws Exception {
		Iterable<ManagedResource<?>> resources = client.getManagedResources();
		// Transaction setup --------------------------------------------------
		// If no resource is declared, the transaction is empty.
		if (resources == null) {
			throw new TransactionEmptyException(EMPTY);
		}
		getListener().bind(this);
		createControllers(resources);
		// Transaction body ---------------------------------------------------
		T result = computeResult(resources);
		// Transaction cleanup ------------------------------------------------
		boolean empty = isEmpty();
		trySafeCommit(resources);
		if (empty) {
			throw new TransactionEmptyException(EMPTY);
		}
		// Return computed result.
		return result;
	}

	/**************************************************************************
	 * Private Methods
	 **************************************************************************/

	/** */
	private T computeResult(Iterable<ManagedResource<?>> resources)
			throws Exception {
		try {
			// Client code ----------------------------------------------------
			return client.call();
		} catch (Exception ex) {
			// Cleanup - Try to roll back.
			rollbackAndRelease(resources, ex);
			throw ex;
		}
	}

	/** */
	private void commit(Iterable<ManagedResource<?>> resources) {
		// Attempt commit on each resource.
		// Execution should abort as soon as an error is encountered.
		// Try ------------------------------------------------------
		for (ManagedResource<?> r : resources) {
			r.commit();
		}
		// If all commits executed successfully, update.
		for (ManagedResource<?> r : resources) {
			r.update();
		}
		// End Try --------------------------------------------------
		// Notify listener.
		getListener().terminate();
	}

	/** */
	private void rollback(Iterable<ManagedResource<?>> resources,
			Exception cause) throws Exception {
		// Attempt to roll back on each resource.
		// Execution is aborted as soon as an error is encountered.
		// Try ------------------------------------------------------
		for (ManagedResource<?> r : resources) {
			r.rollback();
		}
		// End Try --------------------------------------------------
		// Notify listener.
		getListener().terminate();
		throw cause;
	}

	/** Releases all controllers kept. */
	private void releaseControllers(Iterable<ManagedResource<?>> resources) {
		for (ManagedResource<?> r : resources) {
			r.release();
			r.removeController();
		}
		this.controllers.clear();
	}

	/** */
	private void rollbackAndRelease(Iterable<ManagedResource<?>> resources,
			Exception cause) throws Exception {
		try {
			rollback(resources, cause);
		} finally {
			releaseControllers(resources);
		}
	}

	/** */
	private void trySafeCommit(Iterable<ManagedResource<?>> resources)
			throws Exception {
		try {
			commit(resources);
		} catch (Exception ex) {
			rollback(resources, ex);
		} finally {
			releaseControllers(resources);
		}
	}

	/** */
	private void createControllers(Iterable<ManagedResource<?>> resources) {
		ResourceController rc;
		for (ManagedResource<?> r : resources) {
			rc = newController();
			controllers.put(r.getId(), rc);
			r.putController(rc);
		}
	}

	/**************************************************************************
	 * Equals, HashCode, ToString & Clone
	 **************************************************************************/

	/** Creates and returns a (deep) copy of this object. */
	@Override
	public SimpleTransaction<T> clone() {
		return new SimpleTransaction<T>(this);
	}
}