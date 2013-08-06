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

package org.bitbucket.jtransaction.transactions;

import static org.bitbucket.jtransaction.common.Check.checkArgument;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.Copyable;
import org.bitbucket.jtransaction.resources.Resource;
import org.bitbucket.jtransaction.resources.ResourceAcquireException;
import org.bitbucket.jtransaction.resources.ResourceInaccessibleException;
import org.bitbucket.jtransaction.resources.ResourceState;

/**
 * ResourceController allows for reading and writing on a resource.
 * 
 * @author afs
 * @version 2013
*/

class ResourceController implements ResourceHandle, Copyable<ResourceController> {
    private static final NullListener NULL = new NullListener();

    /** Controller status */
    static enum Status {
        UNUSED, READ, CHANGED, COMMITTED, EXPIRED, RELEASED;

        /** */
        static Status initialStatus() {
            return UNUSED;
        }
    }

    // instance variables
    private boolean accessed = false, acquired = false;
    private Status status = Status.initialStatus();
    private final String id;
    private final Resource resource;
    private final ReadWriteListener listener;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ResourceController. */
    protected ResourceController(Resource r, String id) {
        this(r, id, NULL);
    }

    /** Parameter constructor of objects of class ResourceController. */
    protected ResourceController(Resource r, String id, ReadWriteListener rw) {
        checkArgument("null resource", r);
        checkArgument("null id", id);
        checkArgument("null listener", rw);
        this.resource = r;
        this.id = id;
        this.listener = rw;
    }

    /** Copy constructor of objects of class ResourceController. */
    protected ResourceController(ResourceController instance) {
        this(instance.getResource(), instance.getId(), instance.getListener());
    }

    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    protected final Resource getResource() {
        return this.resource;
    }

    /** */
    protected final String getId() {
        return this.id;
    }

    /** */
    protected final ReadWriteListener getListener() {
        return this.listener;
    }

    /** */
    protected final Status getStatus() {
        return this.status;
    }

    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    protected final void setStatus(Status s) {
        this.status = s;
    }

    /** */
    protected final void setAccessed(boolean isAccessed) {
        this.accessed = isAccessed;
    }

    /** */
    protected final void setAcquired(boolean isAcquired) {
        this.acquired = isAcquired;
    }

    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    protected final boolean isResourceAccessible() {
        return this.resource.isAccessible();
    }

    /** */
    protected final boolean isAccessed() {
        return this.accessed;
    }

    /** */
    protected final boolean isAcquired() {
        return this.acquired;
    }

    /** */
    protected final boolean isUnused() {
        return this.status == Status.UNUSED;
    }

    /** */
    protected final boolean isRead() {
        return this.status == Status.READ;
    }

    /** */
    protected final boolean isChanged() {
        return this.status == Status.CHANGED;
    }

    /** */
    protected final boolean isCommitted() {
        return this.status == Status.COMMITTED;
    }

    /** */
    protected final boolean isExpired() {
        return this.status == Status.EXPIRED;
    }

    /** */
    protected final boolean isReleased() {
        return this.status == Status.RELEASED;
    }

    /**************************************************************************
     * Public or Overridable Methods
    **************************************************************************/

    /** Reads the resource, then increments the read counter,
     * and sets the accessed flag.
     * Acquires the resource's lock, if necessary.
     * Throws any ResourceReadException thrown by the resource's read.
     * Throws a TransactionInterruptedException, if the transaction
     * running this operation was interrupted.
     * Throws a ResourceInaccessibleException, if this operation is
     * invoked in a state where the resource is inaccessible.
     * Throws ResourceAcquireException, if the resource can't be acquired.
     * Override for custom behaviour.
    */
    public ResourceState read() {
        // Check for resource accessibility.
        checkResourceAccessible();
        // Check if thread is interrupted.
        checkInterrupted();
        // Check if operation is allowed in current state.
        checkCanRead();
        // Acquire resource, if accessing it for the first time.
        if (!this.accessed) {
            acquireResource();
        }
        // Read the resource and store locally.
        ResourceState result = this.resource.read();
        // The resource has been read, set accessed and notify listener.
        this.accessed = true;
        this.listener.readPerformed(this.id);
        if (!isChanged()) {
            this.status = Status.READ;
        }
        // Return the read state.
        return result;
    }

    /** Writes on the resource, then increments the write counter,
     * and sets the accessed flag.
     * Throws any ResourceWriteException thrown by the resource's write.
     * Throws a TransactionInterruptedException, if the transaction
     * running this operation was interrupted.
     * Throws a ResourceInaccessibleException, if this operation is
     * invoked in a state where the resource is inaccessible.
     * Throws ResourceAcquireException, if the resource can't be acquired.
     * Override for custom behaviour.
    */
    public void write(ResourceState state) {
        // Check for resource accessiblity.
        checkResourceAccessible();
        // Check if thread is interrupted.
        checkInterrupted();
        // Check if operation is allowed in current state.
        checkCanWrite();
        // Acquire resource, if accessing it for the first time.
        if (!this.accessed) {
            acquireResource();
        }
        // Write on the resource.
        this.resource.write(state);
        // The resource has been written, set accessed and notify listener.
        this.accessed = true;
        this.listener.writePerformed(this.id);
        this.status = Status.CHANGED;
    }

    /** Commits the changes made on the resource.
     * Throws any ResourceCommitException thrown by the resource's commit.
     * Throws a TransactionInterruptedException, if the transaction
     * running this operation was interrupted.
     * Throws a ResourceInaccessibleException, if this operation is
     * invoked in a state where the resource is inaccessible.
     */
    protected void commit() {
        // Check for resource accessibility.
        checkResourceAccessible();
        // Check if thread is interrupted.
        checkInterrupted();
        // Check if operation is allowed in current state.
        checkCanCommit();
        // Commit changes.
        this.resource.commit();
        this.status = Status.COMMITTED;
    }

    /** Rolls back the changes made on the resource.
     * Throws any ResourceRollbackException thrown by the resource's rollback.
     * Throws a TransactionInterruptedException, if the transaction
     * running this operation was interrupted.
     * Throws a ResourceInaccessibleException, if this operation is
     * invoked in a state where the resource is inaccessible.
     */
    protected void rollback() {
        // Check for resource accessibility.
        checkResourceAccessible();
        // Check if thread is interrupted.
        checkInterrupted();
        // Check if operation is allowed in current state.
        checkCanRollback();
        if (this.accessed && !isRead()) {
            // Rollback changes.
            this.resource.rollback();
        }
        this.status = Status.EXPIRED;
    }

    /** */
    protected void update() {
        // Check for resource accessibility.
        checkResourceAccessible();
        // Check if thread is interrupted.
        checkInterrupted();
        // Check if operation is allowed in current state.
        checkCanUpdate();
        // Update changes.
        this.resource.update();
        this.status = Status.EXPIRED;
    }

    /** Releases the underlying resource. */
    protected final void release() {
        if (this.acquired) {
            this.resource.release();
        }
        this.status = Status.RELEASED;
    }

    /** Acquires the underlying resource for both read and write operations.
     * Throws ResourceAcquireException, if the resource can't be acquired.
     */
    protected void acquireResource() {
        acquireResource(AccessMode.WRITE);
    }

    /** Acquires the underlying resource for both read and write operations.
     * Throws ResourceAcquireException, if the resource can't be acquired.
     */
    protected final void acquireResource(AccessMode am) {
        if (this.resource.tryAcquireFor(am)) {
            this.acquired = true;
        } else {
            throw new ResourceAcquireException(id);
        }
    }

    /**************************************************************************
     * Checks
    **************************************************************************/

    /** */
    protected final void checkResourceAccessible() {
        if (!this.resource.isAccessible()) {
            throw new ResourceInaccessibleException("Inaccessible resource");
        }
    }

    /** */
    protected final void checkInterrupted() {
        if (Thread.interrupted()) {
            throw new TransactionInterruptedException("interrupted");
        }
    }

    /** */
    protected final void checkCanRead() {
        if (isCommitted() || isExpired() || isReleased()) {
            throw new IllegalHandleStateException();
        }
    }

    /** */
    protected final void checkCanWrite() {
        if (isCommitted() || isExpired() || isReleased()) {
            throw new IllegalHandleStateException();
        }
    }

    /** */
    protected final void checkCanCommit() {
        if (isUnused() || isCommitted() || isExpired() || isReleased()) {
            throw new IllegalHandleStateException();
        }
    }

    /** */
    protected final void checkCanRollback() {
        if (isReleased()) {
            throw new IllegalHandleStateException();
        }
    }

    /** */
    protected final void checkCanUpdate() {
        if (!isCommitted()) {
            throw new IllegalHandleStateException();
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
        if (!(o instanceof ResourceController))
            return false;
        ResourceController n = (ResourceController)o;
        return (this.resource == n.getResource());
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
        return 37 + this.resource.hashCode();
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.resource.toString());
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public ResourceController clone() {
        return new ResourceController(this);
    }

    /**************************************************************************
     * Nested Classes
    **************************************************************************/

    /** Null ReadWriteListener */
    static final class NullListener implements ReadWriteListener {
        NullListener() {}

        /** */
        public void readPerformed(String resource) {}

        /** */
        public void writePerformed(String resource) {}
    }
}
