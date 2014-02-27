package com.github.undo4j;

/**
 * ResourceLock
 * 
 * @author afs
 * @version 2013
 */

abstract class ResourceLock implements Comparable<ResourceLock> {

    /*************************************************************************\
     *  Interfaces
    \*************************************************************************/

    /** */
    static interface AcquireListener {
        void waiting(TransactionId tid, ResourceId resource);
        void notWaiting(TransactionId tid, ResourceId resource);
        void acquired(TransactionId tid, ResourceId resource);
        void released(TransactionId tid, ResourceId resource);
    }



    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    /** */
    private static final AcquireListener NULL_LISTENER = new NullListener();



    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** */
    private final ResourceId id;

    /** */
    private int owners = 0;

    /** */
    private boolean isValid = true;

    /** */
    private final AcquireListener listener;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class ResourceLock.
     */
    protected ResourceLock(final ResourceId id) {
        assert id != null;
        this.id = id;
        this.listener = NULL_LISTENER;
    }


    /**
     *  Parameter constructor of objects of class ResourceLock.
     */
    protected ResourceLock
            (final ResourceId id, final AcquireListener listener) {
        assert id != null && listener != null;
        this.id = id;
        this.listener = listener;
    }



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    protected final ResourceId getId() { return this.id; }


    /** */
    protected final int getOwners() { return this.owners; }


    /**
     * Returns the implemented isolation level on this lock.
     */
    protected abstract IsolationLevel getIsolationLevel();



    /*************************************************************************\
     *  Predicates
    \*************************************************************************/

    /** */
    protected final boolean hasOwner() { return this.owners > 0; }

    /** */
    protected final boolean isValid() { return this.isValid; }



    /*************************************************************************\
     *  Protected Methods
    \*************************************************************************/

    /**
     * Acquires this lock, for the given AccessMode.
     * May throw InterruptedException while waiting.
     * Returns true if acquired; returns false otherwise.
     */
    protected abstract boolean acquire(
        final TransactionId tid,
        final AccessMode mode,
        final WaitStrategy strategy) throws InterruptedException;


    /**
     * Releases this lock, granted it has been acquired.
     */
    protected abstract void release(final TransactionId tid);


    /** */
    protected final void invalidate() {
        this.isValid = false;
    }


    /** */
    protected final void notifyWaiting(final TransactionId tid) {
        assert tid != null;
        this.listener.waiting(tid, this.id);
    }

    /** */
    protected final void notifyNotWaiting(final TransactionId tid) {
        assert tid != null;
        this.listener.notWaiting(tid, this.id);
    }

    /** */
    protected final void notifyAcquired(final TransactionId tid) {
        assert tid != null;
        ++owners;
        this.listener.acquired(tid, this.id);
    }

    /** */
    protected final void notifyReleased(final TransactionId tid) {
        assert tid != null;
        --owners;
        this.listener.released(tid, this.id);
    }



    /*************************************************************************\
     *  Equals, HashCode, ToString & Clone
    \*************************************************************************/

    /**
     *  Equivalence relation.
     *  Contract (for any non-null reference values x, y, and z):
     *      Reflexive: x.equals(x).
     *      Symmetric: x.equals(y) iff y.equals(x).
     *      Transitive: if x.equals(y) and y.equals(z), then x.equals(z).
     *      Consistency: successive calls return the same result,
     *          assuming no modification of the equality fields.
     *      x.equals(null) should return false.
     */
    @Override
    public final boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ResourceLock)) { return false; }
        ResourceLock n = (ResourceLock) o;
        return this.id.equals(n.id);
    }

    /**
     *  Contract:
     *      Consistency: successive calls return the same code,
     *          assuming no modification of the equality fields.
     *      Function: two equal objects have the same (unique) hash code.
     *      (Optional) Injection: unequal objects have different hash codes.
     *
     *  Common practices:
     *      boolean: calculate (f ? 0 : 1);
     *      byte, char, short or int: calculate (int) f;
     *      long: calculate (int) (f ^ (f >>> 32));
     *      float: calculate Float.floatToIntBits(f);
     *      double: calculate Double.doubleToLongBits(f)
     *          and handle the return value like every long value;
     *      Object: use (f == null ? 0 : f.hashCode());
     *      Array: recursion and combine the values.
     *
     *  Formula:
     *      hash = prime * hash + codeForField
     */
    @Override
    public final int hashCode() { return this.id.hashCode(); }

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return this.getIsolationLevel().toString();
    }

    /**
     *  Compares a pair of locks, according to their natural order.
     */
    @Override
    public final int compareTo(final ResourceLock other) {
        return this.id.compareTo(other.id);
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /** */
    private static final class NullListener implements AcquireListener {
        @Override
        public void waiting(TransactionId tid, ResourceId resource) {}

        @Override
        public void notWaiting(TransactionId tid, ResourceId resource) {}

        @Override
        public void acquired(TransactionId tid, ResourceId resource) {}

        @Override
        public void released(TransactionId tid, ResourceId resource) {}
    }
}
