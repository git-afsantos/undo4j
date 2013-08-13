package org.bitbucket.jtransaction.resources;

import org.bitbucket.jtransaction.common.LockManager;

import static org.bitbucket.jtransaction.resources.StateUtil.*;

/**
 * MultiShadowResource
 * 
 * @author afs
 * @version 2013
*/

public final class MultiShadowResource<T>
        extends MultiWriterStatefulResource<T> {
    private static final String INVALID_SHADOW = "invalid shadow state";

    // instance variables
    private final ThreadLocalResourceState<T> shadow =
            new ThreadLocalResourceState<T>();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class MultiShadowResource. */
    public MultiShadowResource(
		InternalResource<T> resource, LockManager lockManager
	) {
        super(resource, lockManager);
    }

    /** Parameter constructor of objects of class MultiShadowResource. */
    public MultiShadowResource(
        InternalResource<T> resource,
        LockManager lockManager,
        boolean buildsEachUpdate
    ) {
        super(resource, lockManager, buildsEachUpdate);
    }


    /** Copy constructor of objects of class MultiShadowResource. */
    private MultiShadowResource(MultiShadowResource<T> instance) {
        super(instance);
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** Returns a copy of the shadow, for the current thread. */
    ResourceState<T> getShadow() {
        return cloneSafely(this.shadow.get());
    }

    /** Returns a direct reference to the shadow, for the current thread.
     */
    ResourceState<T> getShadowReference() {
        return this.shadow.get();
    }


    /** Returns a direct reference of the ThreadLocal shadow. */
    ThreadLocalResourceState<T> getThreadLocalShadow() {
        return this.shadow;
    }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    void setShadow(ResourceState<T> state) {
        this.shadow.set(identity(state));
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    boolean hasShadow() { return !this.shadow.get().isNull(); }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** Stores the given state, as a shadow.
     * The effective write happens on commit, applying the shadow state.
     * This method assumes the resource has been acquired.
     */
    @Override
    public void write(ResourceState<T> state) {
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
    @Override
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
    public MultiShadowResource<T> clone() {
        return new MultiShadowResource<T>(this);
    }
}
