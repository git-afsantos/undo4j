/*
The MIT License (MIT)

Copyright (c) 2013 Andre Santos, Victor Miraldo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/


package org.bitbucket.afsantos.jtransaction.resources;

import java.util.concurrent.locks.ReentrantLock;

/**
 * MultiWriterStatefulResource
 * 
 * @author afs
 * @version 2013
*/

public abstract class MultiWriterStatefulResource extends StatefulResource {
    // instance variables
    private final ReentrantLock lock = new ReentrantLock();
    private final ThreadLocalStatus status = new ThreadLocalStatus();
    private final ThreadLocalResourceState localCommit =
            new ThreadLocalResourceState();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class MultiWriterStatefulResource.
     */
    public MultiWriterStatefulResource(InternalResource resource) {
        super(resource);
    }

    /** Parameter constructor of objects of class MultiWriterStatefulResource.
     */
    public MultiWriterStatefulResource(
        InternalResource resource, boolean buildsEachUpdate
    ) {
        super(resource, buildsEachUpdate);
    }


    /** Copy constructor of objects of class MultiWriterStatefulResource. */
    protected MultiWriterStatefulResource(MultiWriterStatefulResource r) {
        super(r);
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** Returns a copy of the local commit, for the current thread. */
    protected final ResourceState getLocalCommit() {
        return StateUtil.cloneSafely(this.localCommit.get());
    }

    /** Returns a direct reference to the local commit, for the current thread.
     */
    protected final ResourceState getLocalCommitReference() {
        return this.localCommit.get();
    }


    /** Returns a direct reference of the ThreadLocal commit. */
    protected final ThreadLocalResourceState getThreadLocalCommit() {
        return this.localCommit;
    }


    /** */
    @Override
    public final InternalResource getSynchronizedResource() {
        InternalResource res = null;
        try {
            this.lock.lock();
            res = getInternalResource();
        } finally { this.lock.unlock(); }
        return res;
    }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    protected final void setLocalCommit(ResourceState state) {
        this.localCommit.set(state == null ? NULL_STATE : state);
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
    protected void initializeDecorator() {
        try {
            lock();
            super.initializeDecorator();
        } finally { unlock(); }
    }

    /** Disposes of any stored states.
     */
    @Override
    protected void disposeDecorator() {
        try {
            lock();
            super.disposeDecorator();
        } finally { unlock(); }
        this.localCommit.set(NULL_STATE);
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
    protected final void applyState(ResourceState state) throws Exception {
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
    public abstract MultiWriterStatefulResource clone();



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
    static final class ThreadLocalResourceState
            extends ThreadLocal<ResourceState> {
        ThreadLocalResourceState() { super(); }

        @Override
        protected ResourceState initialValue() { return NULL_STATE; }
    }
}
