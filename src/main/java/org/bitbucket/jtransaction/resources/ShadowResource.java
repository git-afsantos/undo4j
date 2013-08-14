package org.bitbucket.jtransaction.resources;

import org.bitbucket.jtransaction.common.LockManager;

import static org.bitbucket.jtransaction.resources.StateUtil.*;

/**
 * ShadowResource is not thread-safe.
 * Implements a resource with shadow writes.
 * 
 * @author afs
 * @version 2013
*/

public final class ShadowResource<T> extends SingleWriterStatefulResource<T> {
    // instance variables
    private ResourceState<T> shadow;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ShadowResource. */
    public ShadowResource(
		InternalResource<T> resource, LockManager lockManager
	) {
        super(resource, lockManager);
        this.shadow = getCheckpointReference();
    }


    /** Copy constructor of objects of class ShadowResource. */
    protected ShadowResource(ShadowResource<T> instance) {
        super(instance);
        this.shadow = instance.getShadow();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    protected final ResourceState<T> getShadow() {
        return cloneSafely(this.shadow);
    }

    /** */
    protected final ResourceState<T> getShadowReference() {
        return this.shadow;
    }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    protected final void setShadow(ResourceState<T> state) {
        this.shadow = identity(state);
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    protected final boolean hasShadow() { return !this.shadow.isNull(); }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** Stores the given state, as a shadow.
     * The effective write happens on commit, applying the shadow state.
     * This method assumes the resource has been acquired.
     */
    @Override
    public final void write(ResourceState<T> state) {
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
    @Override
    public final void commit() {
        // If there's a shadow, apply it, update local commit, and discard it.
        if (isChanged()) {
            if (hasShadow()) {
                applyShadow();
                // A clone of the shadow is set as the local commit.
                // No writer may alter the referenced object after commit.
                setLocalCommit(this.shadow.clone());
                this.shadow = identity(null);
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
    @Override
    public final void rollback() {
        switch (getStatus()) {
            case COMMITTED:
            if (hasLocalCommit()) { rollbackToCheckpoint(); }
            break;
            
            case UPDATED:
            if (hasDifferentPreviousCheckpoint()) { rollbackToPrevious(); }
            break;
            
            default: break;
        }
        // Discard the shadow and the local commit.
        this.shadow = identity(null);
        setLocalCommit(this.shadow);
        setStatus(Status.UPDATED);
        setConsistent(true);
    }


    /** Disposes of any stored states.
     */
    @Override
    protected void disposeDecorator() {
        super.disposeDecorator();
        this.shadow = getCheckpointReference();
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** Validate any changes made, by applying the current shadow. */
    private void applyShadow() {
        try { getInternalResource().applyState(this.shadow); }
        catch (Exception e) {
            setConsistent(false);
            throw new ResourceCommitException(e.getMessage(), e);
        }
    }



    /**************************************************************************
     * Clone
    **************************************************************************/

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public ShadowResource<T> clone() { return new ShadowResource<T>(this); }
}
