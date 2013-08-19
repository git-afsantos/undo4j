package com.github.undo4j.resources;

import com.github.undo4j.common.LockManager;

import static com.github.undo4j.resources.StateUtil.*;

import java.util.concurrent.locks.ReentrantLock;

/**
 * MultiWriterStatefulResource
 * 
 * @author afs
 * @version 2013
*/

public abstract class MultiWriterStatefulResource<T>
        extends StatefulResource<T> {
    // instance variables
    private final ReentrantLock lock = new ReentrantLock();
    private final ThreadLocalStatus status = new ThreadLocalStatus();
    private final ThreadLocalResourceState<T> localCommit =
            new ThreadLocalResourceState<T>();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class MultiWriterStatefulResource.
     */
    public MultiWriterStatefulResource(
		InternalResource<T> resource, LockManager lockManager
	) {
        super(resource, lockManager);
    }

    /** Parameter constructor of objects of class MultiWriterStatefulResource.
     */
    public MultiWriterStatefulResource(
        InternalResource<T> resource,
        LockManager lockManager,
        boolean buildsEachUpdate
    ) {
        super(resource, lockManager, buildsEachUpdate);
    }


    /** Copy constructor of objects of class MultiWriterStatefulResource. */
    protected MultiWriterStatefulResource(MultiWriterStatefulResource<T> r) {
        super(r);
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** Returns a copy of the local commit, for the current thread. */
    protected final ResourceState<T> getLocalCommit() {
        return cloneSafely(this.localCommit.get());
    }

    /** Returns a direct reference to the local commit, for the current thread.
     */
    protected final ResourceState<T> getLocalCommitReference() {
        return this.localCommit.get();
    }


    /** Returns a direct reference of the ThreadLocal commit. */
    protected final ThreadLocalResourceState<T> getThreadLocalCommit() {
        return this.localCommit;
    }


    /** */
    @Override
    public final InternalResource<T> getSynchronizedResource() {
        InternalResource<T> res = null;
        try {
            lock();
            res = getInternalResource();
        } finally { unlock(); }
        return res;
    }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    protected final void setLocalCommit(ResourceState<T> state) {
        this.localCommit.set(identity(state));
    }

    /** */
    protected final void setStatus(Status s) { this.status.set(s); }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    protected final boolean hasLocalCommit() {
        return !this.localCommit.get().isNull();
    }

    /** */
    protected final boolean isUpdated() {
        return this.status.get() == Status.UPDATED;
    }

    /** */
    protected final boolean isChanged() {
        return this.status.get() == Status.CHANGED;
    }

    /** */
    protected final boolean isCommitted() {
        return this.status.get() == Status.COMMITTED;
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public void update() {
        if (isCommitted()) {
            if (hasLocalCommit()) {
                try {
                    lock();
                    applyLocalCommit();
                    updatePreviousCheckpoint();
                    if (buildsEachUpdate()) {
                        updateCheckpoint();
                    } else {
                        // Use clone, so that checkpoint can't be altered.
                        setCheckpoint(this.localCommit.get().clone());
                    }
                    setConsistent(true);
                } finally { unlock(); }
                this.localCommit.remove();
            }
            this.status.remove();
        }
    }


    /** */
    @Override
    protected final void rollbackToCheckpoint() {
        try {
            lock();
            super.rollbackToCheckpoint();
            setConsistent(true);
        } finally { unlock(); }
    }


    /** */
    @Override
    protected final void rollbackToPrevious() {
        try {
            lock();
            super.rollbackToPrevious();
            setConsistent(true);
        } finally { unlock(); }
    }


    /** */
    protected final void removeStatus() { this.status.remove(); }

    /** */
    protected final void removeLocalCommit() { this.localCommit.remove(); }


    /** */
    protected final void lock() { this.lock.lock(); }

    /** */
    protected final void unlock() { this.lock.unlock(); }


    /** */
    protected final void applyState(ResourceState<T> state) throws Exception {
        try {
            lock();
            getInternalResource().applyState(state);
        } finally { unlock(); }
    }


    /** Apply any changes made in the current local commit. */
    private void applyLocalCommit() {
        try { getInternalResource().applyState(this.localCommit.get()); }
        catch (Exception e) {
            setConsistent(false);
            throw new ResourceUpdateException(e.getMessage(), e);
        }
    }



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(this.localCommit.get());
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public abstract MultiWriterStatefulResource<T> clone();



    /**************************************************************************
     * Nested Classes
    **************************************************************************/

    /** */
    static final class ThreadLocalStatus extends ThreadLocal<Status> {
        ThreadLocalStatus() { super(); }

        @Override
        protected Status initialValue() { return Status.UPDATED; }
    }

    /** */
    static final class ThreadLocalResourceState<T>
            extends ThreadLocal<ResourceState<T>> {
        ThreadLocalResourceState() { super(); }

        @Override
        protected ResourceState<T> initialValue() { return identity(null); }
    }
}
