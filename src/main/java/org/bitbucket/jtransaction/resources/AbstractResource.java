package org.bitbucket.jtransaction.resources;

import static org.bitbucket.jtransaction.common.Check.checkArgument;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.Copyable;
import org.bitbucket.jtransaction.common.IsolationLevel;
import org.bitbucket.jtransaction.common.LockManager;

/**
 * AbstractResource
 * 
 * @author afs
 * @version 2013
*/

public abstract class AbstractResource<T>
        implements Resource<T>, Copyable<AbstractResource<T>> {
    // instance variables
	private final ResourceId id = ResourceId.newResourceId();
	private final InternalResource<T> resource;
    private final LockManager lockManager;
    private volatile boolean accessible, consistent;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class AbstractResource.
     */
    public AbstractResource(InternalResource<T> r, LockManager lm) {
        checkArgument("null resource", r);
        checkArgument("null lock manager", lm);
        this.resource = r;
        this.lockManager = lm;
        this.accessible = true;
        this.consistent = true;
    }

    /** Copy constructor of objects of class AbstractResource. */
    protected AbstractResource(AbstractResource<T> instance) {
        this.resource = instance.getInternalResource();
        this.lockManager = instance.getLockManager();
        this.accessible = instance.isAccessible();
        this.consistent = instance.isConsistent();
    }

    /**************************************************************************
     * Getters
    **************************************************************************/

    /**
     * Returns this resource's unique identifier.
     */
    @Override
    public ResourceId getId() { return this.id.clone(); }


    /**
     * Returns the isolation level defined upon construction.
     */
    @Override
    public final IsolationLevel getIsolationLevel() {
        return this.lockManager.getIsolationLevel();
    }

    /** */
    protected final InternalResource<T> getInternalResource() {
        return this.resource;
    }

    /**
     * Subclasses providing concurrent access should override this
     * method to implement the desired synchronization mechanism.
     */
    public InternalResource<T> getSynchronizedResource() {
        return this.resource;
    }

    /**
     * 
     */
    protected final LockManager getLockManager() {
        return this.lockManager.clone();
    }

    /**
     * 
     */
    protected final LockManager getLockManagerReference() {
        return this.lockManager;
    }

    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    @Override
    public final void setAccessible(boolean isAccessible) {
        this.accessible = isAccessible;
    }

    /** */
    @Override
    public final void setConsistent(boolean isConsistent) {
        this.consistent = isConsistent;
    }

    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** Checks whether this resource is accessible. */
    @Override
    public final boolean isAccessible() {
        return this.accessible;
    }

    /** Checks whether this resource is in a consistent state. */
    @Override
    public final boolean isConsistent() {
        return this.consistent;
    }

    /**************************************************************************
     * Public Methods
    **************************************************************************/
    /**
     * Acquire the resource, based on the internal locking scheme.
     */
    @Override
    public final boolean acquire(AccessMode mode) throws InterruptedException {
        return this.lockManager.acquire(mode);
    }

    /**
     * Releases the resource, based on the internal locking scheme.
     */
    @Override
    public final void release() {
        this.lockManager.release();
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
        if (this == o)
            return true;
        if (!(o instanceof AbstractResource))
            return false;
        AbstractResource<?> n = (AbstractResource<?>)o;
        return this.resource.equals(n.getInternalResource());
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
        return resource.hashCode();
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        return this.resource.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public abstract AbstractResource<T> clone();
}
