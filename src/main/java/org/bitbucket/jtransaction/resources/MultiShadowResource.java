/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Andre Santos, Victor Miraldo
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.bitbucket.jtransaction.resources;

/**
 * MultiShadowResource
 * 
 * @author afs
 * @version 2013
*/

public final class MultiShadowResource extends MultiWriterStatefulResource {
    private static final String INVALID_SHADOW = "invalid shadow state";

    // instance variables
    private final ThreadLocalResourceState shadow = new ThreadLocalResourceState();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class MultiShadowResource. */
    public MultiShadowResource(InternalResource resource) {
        super(resource);
    }

    /** Parameter constructor of objects of class MultiShadowResource. */
    public MultiShadowResource(InternalResource resource, boolean buildsEachUpdate) {
        super(resource, buildsEachUpdate);
    }

    /** Copy constructor of objects of class MultiShadowResource. */
    private MultiShadowResource(MultiShadowResource instance) {
        super(instance);
    }

    /**************************************************************************
     * Getters
    **************************************************************************/

    /** Returns a copy of the shadow, for the current thread. */
    ResourceState getShadow() {
        return StateUtil.cloneSafely(this.shadow.get());
    }

    /** Returns a direct reference to the shadow, for the current thread.
     */
    ResourceState getShadowReference() {
        return this.shadow.get();
    }

    /** Returns a direct reference of the ThreadLocal shadow. */
    ThreadLocalResourceState getThreadLocalShadow() {
        return this.shadow;
    }

    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    void setShadow(ResourceState state) {
        this.shadow.set(state == null ? NULL_STATE : state);
    }

    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    boolean hasShadow() {
        return !this.shadow.get().isNull();
    }

    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** Stores the given state, as a shadow.
     * The effective write happens on commit, applying the shadow state.
     * This method assumes the resource has been acquired.
     */
    public void write(ResourceState state) {
        setShadow(state);
        setStatus(Status.CHANGED);
    }

    /** Attempts to commit the changes made on this resource,
     * invoking applyState on the internal resource.
     * Also discards the stored shadow, after committing.
     * Throws a ResourceCommitException, if any exception occurs,
     * while applying the changes.
     * This method assumes the resource has been acquired.
     */
    public void commit() {
        // If there's a shadow, validate it, update local commit and discard it.
        if (isChanged()) {
            if (hasShadow()) {
                validateShadow();
                // Clone shadow, so that committed state can't be altered.
                setLocalCommit(this.shadow.get().clone());
                this.shadow.remove();
            }
            setStatus(Status.COMMITTED);
        }
    }

    /** Reverts the changes made on this resource.
     * Throws a ResourceRollbackException, if any exception occurs
     * while reverting the changes.
     * This method assumes the resource has been acquired.
     */
    public void rollback() {
        if (isUpdated() && hasDifferentPreviousCheckpoint()) {
            rollbackToPrevious();
        }
        // Discard the shadow and the local commit.
        this.shadow.remove();
        removeLocalCommit();
        removeStatus();
        setConsistent(true);
    }

    /** Disposes of any stored states.
     */
    @Override
    protected void disposeDecorator() {
        super.disposeDecorator();
        this.shadow.remove();
    }

    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** Validate any changes made by the current shadow. */
    private void validateShadow() {
        if (!getInternalResource().isValidState(this.shadow.get())) {
            throw new ResourceCommitException(INVALID_SHADOW);
        }
    }

    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public MultiShadowResource clone() {
        return new MultiShadowResource(this);
    }
}
