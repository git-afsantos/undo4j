package org.bitbucket.jtransaction.resources;

import org.bitbucket.jtransaction.common.LockManager;

import static org.bitbucket.jtransaction.resources.StateUtil.*;

/**
 * StatefulResource
 * 
 * @author afs
 * @version 2013
*/

public abstract class StatefulResource<T> extends AbstractResource<T> {
    /** Resource status enumeration. */
    enum Status {
        UPDATED, CHANGED, COMMITTED;

        /** Returns the initial status. */
        static Status initialStatus() { return UPDATED; }
    }

    // instance variables
    /**
     * Flag to tell whether each update is built from the internal resource.
     * Checkpoints may be set directly from the committed state, or
     * by reading again the internal resource, since the commit operation
     * modifies it.
     */
    private final boolean buildEach;
    private volatile ResourceState<T> previous, checkpoint;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class StatefulResource. */
    public StatefulResource(
		InternalResource<T> resource, LockManager lockManager
	) {
        this(resource, lockManager, false);
    }

    /** Parameter constructor of objects of class StatefulResource. */
    public StatefulResource(
        InternalResource<T> resource,
        LockManager lockManager,
        boolean buildEachUpdate
    ) {
        super(resource, lockManager);
        this.buildEach = buildEachUpdate;
        this.initializeCheckpoint();
        this.previous = this.checkpoint;
    }


    /** Copy constructor of objects of class StatefulResource. */
    protected StatefulResource(StatefulResource<T> instance) {
        super(instance);
        this.buildEach = instance.buildsEachUpdate();
        this.checkpoint = instance.getCheckpoint();
        this.previous = instance.getPreviousCheckpoint();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** Returns a copy of the checkpoint. */
    protected final ResourceState<T> getCheckpoint() {
        return cloneSafely(this.checkpoint);
    }

    /** Returns a direct reference to the checkpoint. */
    protected final ResourceState<T> getCheckpointReference() {
        return this.checkpoint;
    }


    /** Returns a copy of the previous checkpoint. */
    protected final ResourceState<T> getPreviousCheckpoint() {
        return cloneSafely(this.previous);
    }

    /** Returns a direct reference to the previous checkpoint. */
    protected final ResourceState<T> getPreviousCheckpointReference() {
        return this.previous;
    }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    protected final void setCheckpoint(ResourceState<T> state) {
        this.checkpoint = identity(state);
    }


    /** */
    protected final void setPreviousCheckpoint(ResourceState<T> state) {
        this.previous = identity(state);
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    protected final boolean hasCheckpoint() {
        return !this.checkpoint.isNull();
    }


    /** */
    protected final boolean hasPreviousCheckpoint() {
        return !this.previous.isNull();
    }

    /** */
    protected final boolean hasDifferentPreviousCheckpoint() {
        return !this.previous.isNull() &&
                !this.previous.equals(this.checkpoint);
    }


    /** */
    protected final boolean buildsEachUpdate() {
        return this.buildEach;
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** This method assumes the resource has been acquired.
     * Chapter 17 of The Java Language Specification:
     * "Writes and reads of volatile fields have similar memory consistency
     * effects as entering and exiting monitors".
     * This guarantees that cloning the checkpoint does not need monitors,
     * granted that no writers are able to modify the object
     * referenced by the checkpoint.
     */
    @Override
    public final ResourceState<T> read() { return getCheckpoint(); }


    /** */
    protected final void updatePreviousCheckpoint() {
        this.previous = this.checkpoint;
    }

    /** */
    protected final void revertCheckpoint() {
        this.checkpoint = this.previous;
    }

    /** */
    protected final void updateCheckpoint() {
        try { buildCheckpoint(); }
        catch (Exception e) {
            setConsistent(false);
            throw new ResourceUpdateException(e.getMessage(), e);
        }
    }

    /**
     * Subclasses should override this method to ensure that only one
     * thread is applying a state at any time.
     */
    protected void rollbackToCheckpoint() {
        try {
            getInternalResource().applyState(this.checkpoint);
        } catch (Exception e) {
            setConsistent(false);
            throw new ResourceRollbackException(e.getMessage(), e);
        }
    }

    /**
     * Subclasses should override this method to ensure that only one
     * thread is applying a state at any time.
     */
    protected void rollbackToPrevious() {
        try {
            getInternalResource().applyState(this.previous);
            this.checkpoint = this.previous;
        } catch (Exception e) {
            setConsistent(false);
            throw new ResourceRollbackException(e.getMessage(), e);
        }
    }


    /** */
    protected final void buildCheckpoint() throws Exception {
        this.checkpoint = identity(getInternalResource().buildState());
    }


    /** Creates the initial checkpoint.
     */
    protected final void initializeCheckpoint() {
        try { buildCheckpoint(); }
        catch (Exception e) {
            throw new ResourceInitializeException(e.getMessage(), e);
        }
    }



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Equivalence relation. Contract (for any non-null reference values
            x, y, and z):
        * Reflexive: x.equals(x).
        * Symmetric: x.equals(y) iff y.equals(x).
        * Transitive: if x.equals(y) and y.equals(z), then x.equals(z).
        * Consistency: successive calls (with no modification of the equality
            fields) return the same result.
        * x.equals(null) should return false.
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatefulResource)) return false;
        StatefulResource<?> n = (StatefulResource<?>) o;
        return (getInternalResource().equals(n.getInternalResource()) &&
                this.checkpoint.equals(n.getCheckpointReference()));
    }

    /** Contract:
        * Consistency: successive calls (with no modification of the equality
            fields) return the same code.
        * Function: two equal objects have the same (unique) hash code.
        * (Optional) Injection: unequal objects have different hash codes.
    * Common practices:
        * boolean: calculate (f ? 0 : 1);
        * byte, char, short or int: calculate (int) f;
        * long: calculate (int) (f ^ (f >>> 32));
        * float: calculate Float.floatToIntBits(f);
        * double: calculate Double.doubleToLongBits(f)
            and handle the return value like every long value;
        * Object: use (f == null ? 0 : f.hashCode());
        * Array: recursion and combine the values.
    * Formula:
        hash = prime * hash + codeForField
    */
    @Override
    public int hashCode() {
        return 37 * super.hashCode() + this.checkpoint.hashCode();
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(this.checkpoint);
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public abstract StatefulResource<T> clone();
}
