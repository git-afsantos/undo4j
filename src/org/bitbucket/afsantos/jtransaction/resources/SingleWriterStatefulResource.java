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


/**
 * SingleWriterStatefulResource
 * 
 * @author afs
 * @version 2013
*/

public abstract class SingleWriterStatefulResource extends StatefulResource {
    // instance variables
    private Status status;
    private ResourceState localCommit;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class SingleWriterStatefulResource.
     */
    public SingleWriterStatefulResource(InternalResource resource) {
        super(resource);
        this.status = Status.initialStatus();
        this.localCommit = NULL_STATE;
    }

    /** Parameter constructor of objects of class SingleWriterStatefulResource.
     */
    public SingleWriterStatefulResource(
        InternalResource resource, boolean buildsEachUpdate
    ) {
        super(resource, buildsEachUpdate);
        this.status = Status.initialStatus();
        this.localCommit = NULL_STATE;
    }


    /** Copy constructor of objects of class SingleWriterStatefulResource. */
    protected SingleWriterStatefulResource(SingleWriterStatefulResource r) {
        super(r);
        this.status = r.getStatus();
        this.localCommit = r.getLocalCommit();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** Returns a copy of the local commit. */
    protected final ResourceState getLocalCommit() {
        return StateUtil.cloneSafely(this.localCommit);
    }

    /** Returns a direct reference to the local commit. */
    protected final ResourceState getLocalCommitReference() {
        return this.localCommit;
    }

    /** Returns the current status. */
    protected final Status getStatus() { return this.status; }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    protected final void setLocalCommit(ResourceState state) {
        this.localCommit = (state == null ? NULL_STATE : state);
    }

    /** */
    protected final void setStatus(Status s) { this.status = s; }



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

    /** This method assumes the resource has been acquired.
     * A clone of the local commit is set as the checkpoint, so that no
     * writers may alter the checkpoint object, after it has been set.
     * This guarantees that subsequent concurrent reads from the checkpoint
     * do not require synchronization on a monitor.
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
                this.localCommit = NULL_STATE;
            }
            this.status = Status.UPDATED;
        }
    }


    /** Disposes of any stored states.
     */
    @Override
    protected void disposeDecorator() {
        super.disposeDecorator();
        this.localCommit = NULL_STATE;
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
    public abstract SingleWriterStatefulResource clone();
}
