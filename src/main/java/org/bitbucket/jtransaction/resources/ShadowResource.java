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
 * ShadowResource is not thread-safe.
 * Implements a resource with shadow writes.
 * 
 * @author afs
 * @version 2013
*/

public class ShadowResource extends SingleWriterStatefulResource {
    // instance variables
    private ResourceState shadow;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ShadowResource. */
    public ShadowResource(InternalResource resource) {
        super(resource);
        this.shadow = NULL_STATE;
    }

    /** Copy constructor of objects of class ShadowResource. */
    protected ShadowResource(ShadowResource instance) {
        super(instance);
        this.shadow = instance.getShadow();
    }

    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    protected final ResourceState getShadow() {
        return StateUtil.cloneSafely(this.shadow);
    }

    /** */
    protected final ResourceState getShadowReference() {
        return this.shadow;
    }

    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    protected final void setShadow(ResourceState state) {
        this.shadow = (state == null ? NULL_STATE : state);
    }

    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    protected final boolean hasShadow() {
        return !this.shadow.isNull();
    }

    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** Stores the given state, as a shadow.
     * The effective write happens on commit, applying the shadow state.
     * This method assumes the resource has been acquired.
     */
    public final void write(ResourceState state) {
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
    public final void commit() {
        // If there's a shadow, apply it, update local commit, and discard it.
        if (isChanged()) {
            if (hasShadow()) {
                applyShadow();
                // A clone of the shadow is set as the local commit.
                // No writer may alter the referenced object after commit.
                setLocalCommit(this.shadow.clone());
                this.shadow = NULL_STATE;
            }
            setStatus(Status.COMMITTED);
            setConsistent(true);
        }
    }

    /** Reverts the changes made on this resource.
     * Throws a ResourceRollbackException, if any exception occurs
     * while reverting the changes.
     * This method does not distinguish between readers and writers.
     * Such distinction (for instance, rollback behaving as an identity
     * for readers) is beyond the scope of this class.
     * This method assumes the resource has been acquired.
     */
    public final void rollback() {
        switch (getStatus()) {
        case COMMITTED:
            if (hasLocalCommit()) {
                rollbackToCheckpoint();
            }
            break;

        case UPDATED:
            if (hasDifferentPreviousCheckpoint()) {
                rollbackToPrevious();
            }
            break;

        default:
            break;
        }
        // Discard the shadow and the local commit.
        this.shadow = NULL_STATE;
        setLocalCommit(NULL_STATE);
        setStatus(Status.UPDATED);
        setConsistent(true);
    }

    /** Disposes of any stored states.
     */
    @Override
    protected void disposeDecorator() {
        super.disposeDecorator();
        this.shadow = NULL_STATE;
    }

    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** Validate any changes made, by applying the current shadow. */
    private void applyShadow() {
        try {
            getInternalResource().applyState(this.shadow);
        } catch (Exception e) {
            setConsistent(false);
            throw new ResourceCommitException(e.getMessage(), e);
        }
    }

    /**************************************************************************
     * Clone
    **************************************************************************/

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public ShadowResource clone() {
        return new ShadowResource(this);
    }
}
