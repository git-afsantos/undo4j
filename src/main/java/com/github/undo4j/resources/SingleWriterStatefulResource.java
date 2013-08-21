package com.github.undo4j.resources;

import com.github.undo4j.common.LockManager;

import static com.github.undo4j.resources.StateUtil.*;

/**
 * SingleWriterStatefulResource
 * 
 * @author afs
 * @version 2013
 */

public abstract class SingleWriterStatefulResource<T> extends StatefulResource<T> {
	// instance variables
	private Status status;
	private ResourceState<T> localCommit;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/**
	 * Parameter constructor of objects of class SingleWriterStatefulResource.
	 */
	public SingleWriterStatefulResource(InternalResource<T> resource, LockManager lockManager) {
		super(resource, lockManager);
		this.status = Status.initialStatus();
		this.localCommit = getCheckpointReference();
	}

	/**
	 * Parameter constructor of objects of class SingleWriterStatefulResource.
	 */
	public SingleWriterStatefulResource(InternalResource<T> resource, LockManager lockManager, boolean buildsEachUpdate) {
		super(resource, lockManager, buildsEachUpdate);
		this.status = Status.initialStatus();
		this.localCommit = getCheckpointReference();
	}

	/** Copy constructor of objects of class SingleWriterStatefulResource. */
	protected SingleWriterStatefulResource(SingleWriterStatefulResource<T> r) {
		super(r);
		this.status = r.getStatus();
		this.localCommit = r.getLocalCommit();
	}

	/**************************************************************************
	 * Getters
	 **************************************************************************/

	/** Returns a copy of the local commit. */
	protected final ResourceState<T> getLocalCommit() {
		return cloneSafely(this.localCommit);
	}

	/** Returns a direct reference to the local commit. */
	protected final ResourceState<T> getLocalCommitReference() {
		return this.localCommit;
	}

	/** Returns the current status. */
	protected final Status getStatus() {
		return this.status;
	}

	/**************************************************************************
	 * Setters
	 **************************************************************************/

	/** */
	protected final void setLocalCommit(ResourceState<T> state) {
		this.localCommit = identity(state);
	}

	/** */
	protected final void setStatus(Status s) {
		this.status = s;
	}

	/**************************************************************************
	 * Predicates
	 **************************************************************************/

	/** */
	protected final boolean hasLocalCommit() {
		return !this.localCommit.isNull();
	}

	/** */
	protected final boolean isUpdated() {
		return this.status == Status.UPDATED;
	}

	/** */
	protected final boolean isChanged() {
		return this.status == Status.CHANGED;
	}

	/** */
	protected final boolean isCommitted() {
		return this.status == Status.COMMITTED;
	}

	/**************************************************************************
	 * Public Methods
	 **************************************************************************/

	/**
	 * This method assumes the resource has been acquired. A clone of the local
	 * commit is set as the checkpoint, so that no writers may alter the
	 * checkpoint object, after it has been set. This guarantees that subsequent
	 * concurrent reads from the checkpoint do not require synchronization on a
	 * monitor.
	 */
	@Override
	public final void update() {
		if (isCommitted()) {
			if (hasLocalCommit()) {
				updatePreviousCheckpoint();
				if (buildsEachUpdate()) {
					updateCheckpoint();
				} else {
					// Clone the local commit. Otherwise, external writers
					// could modify the checkpoint after being set.
					setCheckpoint(this.localCommit.clone());
				}
				// Set a NullState on lockCommit;
				this.localCommit = identity(null);
			}
			this.status = Status.UPDATED;
		}
	}

	/**************************************************************************
	 * Equals, HashCode, ToString & Clone
	 **************************************************************************/

	/** Returns a string representation of the object. */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(this.localCommit);
		return sb.toString();
	}

	/** Creates and returns a (deep) copy of this object. */
	@Override
	public abstract SingleWriterStatefulResource<T> clone();
}
