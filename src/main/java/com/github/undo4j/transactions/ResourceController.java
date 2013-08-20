package com.github.undo4j.transactions;

import com.github.undo4j.common.AccessMode;
import com.github.undo4j.common.Copyable;
import com.github.undo4j.resources.Resource;
import com.github.undo4j.resources.ResourceAcquireException;
import com.github.undo4j.resources.ResourceInaccessibleException;
import com.github.undo4j.resources.ResourceState;

/**
 * ResourceController allows for reading and writing on a resource.
 * 
 * @author afs
 * @version 2013
 */

class ResourceController implements Copyable<ResourceController> {
	/** Controller status */
	static enum Status {
		UNUSED, READ, CHANGED, COMMITTED, EXPIRED, RELEASED;

		/** */
		static Status initialStatus() {
			return UNUSED;
		}
	}

	// instance variables
	private boolean accessed = false, acquired = false;
	private Status status = Status.initialStatus();

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class ResourceController. */
	protected ResourceController() {
	}

	/** Copy constructor of objects of class ResourceController. */
	protected ResourceController(ResourceController instance) {
		this.accessed = instance.isAccessed();
		this.acquired = instance.isAcquired();
		this.status = instance.getStatus();
	}

	/**************************************************************************
	 * Getters
	 **************************************************************************/

	/** */
	protected final Status getStatus() {
		return this.status;
	}

	/**************************************************************************
	 * Setters
	 **************************************************************************/

	/** */
	protected final void setStatus(Status s) {
		this.status = s;
	}

	/** */
	protected final void setAccessed(boolean isAccessed) {
		this.accessed = isAccessed;
	}

	/** */
	protected final void setAcquired(boolean isAcquired) {
		this.acquired = isAcquired;
	}

	/**************************************************************************
	 * Predicates
	 **************************************************************************/

	/** */
	protected final <T> boolean isResourceAccessible(Resource<T> resource) {
		return resource.isAccessible();
	}

	/** */
	protected final boolean isAccessed() {
		return this.accessed;
	}

	/** */
	protected final boolean isAcquired() {
		return this.acquired;
	}

	/** */
	protected final boolean isUnused() {
		return this.status == Status.UNUSED;
	}

	/** */
	protected final boolean isRead() {
		return this.status == Status.READ;
	}

	/** */
	protected final boolean isChanged() {
		return this.status == Status.CHANGED;
	}

	/** */
	protected final boolean isCommitted() {
		return this.status == Status.COMMITTED;
	}

	/** */
	protected final boolean isExpired() {
		return this.status == Status.EXPIRED;
	}

	/** */
	protected final boolean isReleased() {
		return this.status == Status.RELEASED;
	}

	/**************************************************************************
	 * Public or Overridable Methods
	 **************************************************************************/

	/**
	 * Reads the resource, then increments the read counter, and sets the
	 * accessed flag. Acquires the resource's lock, if necessary. Throws any
	 * ResourceReadException thrown by the resource's read. Throws a
	 * TransactionInterruptedException, if the transaction running this
	 * operation was interrupted. Throws a ResourceInaccessibleException, if
	 * this operation is invoked in a state where the resource is inaccessible.
	 * Throws ResourceAcquireException, if the resource can't be acquired.
	 * Override for custom behaviour.
	 */
	protected <T> ResourceState<T> read(Resource<T> resource) {
		// Check for resource accessibility.
		checkResourceAccessible(resource);
		// Check if thread is interrupted.
		checkInterrupted();
		// Check if operation is allowed in current state.
		checkCanRead();
		// Acquire resource, if accessing it for the first time.
		if (!this.accessed) {
			acquireResource(resource);
		}
		// Read the resource and store locally.
		ResourceState<T> result = resource.read();
		// The resource has been read, set accessed and notify listener.
		this.accessed = true;
		// this.listener.readPerformed(this.id);
		if (!isChanged()) {
			this.status = Status.READ;
		}
		// Return the read state.
		return result;
	}

	/**
	 * Writes on the resource, then increments the write counter, and sets the
	 * accessed flag. Throws any ResourceWriteException thrown by the resource's
	 * write. Throws a TransactionInterruptedException, if the transaction
	 * running this operation was interrupted. Throws a
	 * ResourceInaccessibleException, if this operation is invoked in a state
	 * where the resource is inaccessible. Throws ResourceAcquireException, if
	 * the resource can't be acquired. Override for custom behaviour.
	 */
	protected <T> void write(Resource<T> resource, ResourceState<T> state) {
		// Check for resource accessiblity.
		checkResourceAccessible(resource);
		// Check if thread is interrupted.
		checkInterrupted();
		// Check if operation is allowed in current state.
		checkCanWrite();
		// Acquire resource, if accessing it for the first time.
		if (!this.accessed) {
			acquireResource(resource);
		}
		this.accessed = true;
		// Write on the resource.
		resource.write(state);
		// this.listener.writePerformed(this.id);
		this.status = Status.CHANGED;
	}

	/**
	 * Commits the changes made on the resource. Throws any
	 * ResourceCommitException thrown by the resource's commit. Throws a
	 * TransactionInterruptedException, if the transaction running this
	 * operation was interrupted. Throws a ResourceInaccessibleException, if
	 * this operation is invoked in a state where the resource is inaccessible.
	 */
	protected <T> void commit(Resource<T> resource) {
		// Check for resource accessibility.
		checkResourceAccessible(resource);
		// Check if thread is interrupted.
		checkInterrupted();
		// Check if operation is allowed in current state.
		checkCanCommit();
		// Commit changes.
		resource.commit();
		this.status = Status.COMMITTED;
	}

	/**
	 * Rolls back the changes made on the resource. Throws any
	 * ResourceRollbackException thrown by the resource's rollback. Throws a
	 * TransactionInterruptedException, if the transaction running this
	 * operation was interrupted. Throws a ResourceInaccessibleException, if
	 * this operation is invoked in a state where the resource is inaccessible.
	 */
	protected <T> void rollback(Resource<T> resource) {
		// Check for resource accessibility.
		checkResourceAccessible(resource);
		// Check if thread is interrupted.
		checkInterrupted();
		// Check if operation is allowed in current state.
		checkCanRollback();
		if (this.accessed && !isRead()) {
			// Roll back changes.
			resource.rollback();
		}
		this.status = Status.EXPIRED;
	}

	/** */
	protected <T> void update(Resource<T> resource) {
		// Check for resource accessibility.
		checkResourceAccessible(resource);
		// Check if thread is interrupted.
		checkInterrupted();
		// Check if operation is allowed in current state.
		checkCanUpdate();
		// Update changes.
		resource.update();
		this.status = Status.EXPIRED;
	}

	/** Releases the underlying resource. */
	protected final <T> void release(Resource<T> resource) {
		if (this.acquired) {
			resource.release();
		}
		this.status = Status.RELEASED;
	}

	/**
	 * Acquires the underlying resource for both read and write
	 * executedOperations. Throws ResourceAcquireException, if the resource
	 * can't be acquired.
	 */
	protected <T> void acquireResource(Resource<T> resource) {
		acquireResource(resource, AccessMode.WRITE);
	}

	/**
	 * Acquires the underlying resource for both read and write
	 * executedOperations. Throws ResourceAcquireException, if the resource
	 * can't be acquired.
	 */
	protected final <T> void acquireResource(Resource<T> r, AccessMode am) {
		try {
			if (r.acquire(am)) {
				this.acquired = true;
			} else {
				throw new ResourceAcquireException();
			}
		} catch (InterruptedException e) {
			throw new ResourceAcquireException();
		}
	}

	/**************************************************************************
	 * Checks
	 **************************************************************************/

	/** */
	protected final <T> void checkResourceAccessible(Resource<T> resource) {
		if (!resource.isAccessible()) {
			throw new ResourceInaccessibleException("Inaccessible resource");
		}
	}

	/** */
	protected final void checkInterrupted() {
		if (Thread.interrupted()) {
			throw new TransactionInterruptedException("interrupted");
		}
	}

	/** */
	protected final void checkCanRead() {
		if (isCommitted() || isExpired() || isReleased()) {
			throw new IllegalResourceStateException();
		}
	}

	/** */
	protected final void checkCanWrite() {
		if (isCommitted() || isExpired() || isReleased()) {
			throw new IllegalResourceStateException();
		}
	}

	/** */
	protected final void checkCanCommit() {
		if (isUnused() || isCommitted() || isExpired() || isReleased()) {
			throw new IllegalResourceStateException();
		}
	}

	/** */
	protected final void checkCanRollback() {
		if (isReleased()) {
			throw new IllegalResourceStateException();
		}
	}

	/** */
	protected final void checkCanUpdate() {
		if (!isCommitted()) {
			throw new IllegalResourceStateException();
		}
	}

	/**************************************************************************
	 * Equals, HashCode, ToString & Clone
	 **************************************************************************/

	/**
	 * Equivalence relation. Contract (for any non-null reference values x, y,
	 * and z): Reflexive: x.equals(x). Symmetric: x.equals(y) iff y.equals(x).
	 * Transitive: if x.equals(y) and y.equals(z), then x.equals(z).
	 * Consistency: successive calls (with no modification of the equality
	 * fields) return the same result. x.equals(null) should return false.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ResourceController))
			return false;
		ResourceController n = (ResourceController) o;
		return (this.status == n.getStatus());
	}

	/**
	 * Contract: Consistency: successive calls (with no modification of the
	 * equality fields) return the same code. Function: two equal objects have
	 * the same (unique) hash code. (Optional) Injection: unequal objects have
	 * different hash codes. Common practices: boolean: calculate (f ? 0 : 1);
	 * byte, char, short or int: calculate (int) f; long: calculate (int) (f ^
	 * (f >>> 32)); float: calculate Float.floatToIntBits(f); double: calculate
	 * Double.doubleToLongBits(f) and handle the return value like every long
	 * value; Object: use (f == null ? 0 : f.hashCode()); Array: recursion and
	 * combine the values. Formula: hash = prime * hash + codeForField
	 */
	@Override
	public int hashCode() {
		return this.status.hashCode();
	}

	/** Returns a string representation of the object. */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.status.toString());
		return sb.toString();
	}

	/** Creates and returns a (deep) copy of this object. */
	@Override
	public ResourceController clone() {
		return new ResourceController(this);
	}

	/**************************************************************************
	 * Nested Classes
	 **************************************************************************/

	/** Null ReadWriteListener */
	/*
	 * static final class NullListener implements ReadWriteListener {
	 * NullListener() {}
	 * 
	 * @Override public void readPerformed(String resource) {}
	 * 
	 * @Override public void writePerformed(String resource) {} }
	 */
}
