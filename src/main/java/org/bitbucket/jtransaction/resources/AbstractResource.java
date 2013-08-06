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

import static org.bitbucket.jtransaction.common.Check.checkArgument;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.Copyable;
import org.bitbucket.jtransaction.common.IsolationLevel;

/**
 * AbstractResource
 * 
 * @author afs
 * @version 2013
*/

public abstract class AbstractResource implements Resource, Copyable<AbstractResource> {
    // instance variables
    private final InternalResource resource;
    private volatile boolean accessible, consistent;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class AbstractResource.
     */
    public AbstractResource(InternalResource r) {
        checkArgument("null resource", r);
        this.resource = r;
        this.accessible = false;
        this.consistent = true;
    }

    /** Copy constructor of objects of class AbstractResource. */
    protected AbstractResource(AbstractResource instance) {
        this.resource = instance.getInternalResource();
        this.accessible = instance.isAccessible();
        this.consistent = instance.isConsistent();
    }

    /**************************************************************************
     * Getters
    **************************************************************************/

    /** Returns IsolationLevel.NONE, by default.
     * Implementations should override this method to define their
     * isolation level.
     */
    public IsolationLevel getIsolationLevel() {
        return IsolationLevel.NONE;
    }

    /** */
    protected final InternalResource getInternalResource() {
        return this.resource;
    }

    /**
     * Subclasses providing concurrent access should override this
     * method to implement the desired synchronization mechanism.
     */
    public InternalResource getSynchronizedResource() {
        return this.resource;
    }

    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    public final void setAccessible(boolean isAccessible) {
        this.accessible = isAccessible;
    }

    /** */
    public final void setConsistent(boolean isConsistent) {
        this.consistent = isConsistent;
    }

    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** Checks whether this resource is accessible. */
    public final boolean isAccessible() {
        return this.accessible;
    }

    /** Checks whether this resource is in a consistent state. */
    public final boolean isConsistent() {
        return this.consistent;
    }

    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** Invokes initialize on the internal resource,
     * and then sets this resource as accessible.
     * Throws a ResourceInitializeException wrapping any exception thrown
     * by the internal resource's initialize.
     */
    public final void initialize() {
        // Initialize internal resource.
        initializeInternalResource();
        // Notify subclasses.
        initializeDecorator();
        // Set the resource as accessible.
        this.accessible = true;
    }

    /** Invokes dispose on the internal resource,
     * and then sets the resource as inaccessible.
     * Throws a ResourceDisposeException wrapping any exception thrown
     * by the internal resource's dispose.
     */
    public final void dispose() {
        // Set the resource as inaccessible.
        this.accessible = false;
        // Dispose internal resource.
        disposeInternalResource();
        // Notify subclasses.
        disposeDecorator();
    }

    /** Does nothing, by default.
     * Implementations should override to support concurrent access.
     */
    public void acquireFor(AccessMode mode) {}

    /** Returns true, by default.
     * Implementations should override to support concurrent access.
     */
    public boolean tryAcquireFor(AccessMode mode, long millis) {
        return true;
    }

    /** Returns true, by default.
     * Implementations should override to support concurrent access.
     */
    public boolean tryAcquireFor(AccessMode mode) {
        return true;
    }

    /** Does nothing, by default.
     * Implementations should override to support concurrent access.
     */
    public void release() {}

    /** Called in initialize.
     * Does nothing, by default.
     * Override for custom behaviour.
     */
    protected void initializeDecorator() {}

    /** Called in dispose.
     * Does nothing, by default.
     * Override for custom behaviour.
     */
    protected void disposeDecorator() {}

    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    private void initializeInternalResource() {
        try {
            // Perform any necessary initialization.
            this.resource.initialize();
        } catch (Exception e) {
            throw new ResourceInitializeException(e.getMessage(), e);
        }
    }

    /** */
    private void disposeInternalResource() {
        try {
            // Perform any necessary cleanup.
            this.resource.dispose();
        } catch (Exception e) {
            throw new ResourceDisposeException(e.getMessage(), e);
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
        if (this == o)
            return true;
        if (!(o instanceof AbstractResource))
            return false;
        AbstractResource n = (AbstractResource)o;
        return (this.resource.equals(n.getInternalResource()));
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
        return 37 + resource.hashCode();
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        /*StringBuilder sb = new StringBuilder();
        sb.append(this.resource.toString());
        return sb.toString();*/
        return this.resource.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public abstract AbstractResource clone();
}
