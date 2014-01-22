package com.github.undo4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lock
 * 
 * @author afs
 * @version 2013
 */

abstract class Lock implements Comparable<Lock> {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    /** */
    private static final AtomicInteger ID =
        new AtomicInteger(Integer.MIN_VALUE);



    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** */
    private final int id = ID.getAndIncrement();

    /** */
    protected volatile boolean isValid = true;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class Lock.
     */
    protected Lock() {}



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    protected final int getId() { return this.id; }


    /**
     * Returns the implemented isolation level on this lock.
     */
    protected abstract IsolationLevel getIsolationLevel();



    /*************************************************************************\
     *  Protected Methods
    \*************************************************************************/

    /**
     * Acquires this lock, for the given AccessMode.
     * May throw InterruptedException while waiting.
     * Returns true if acquired; returns false otherwise.
     */
    protected abstract boolean acquire
        (AccessMode mode, LockStrategy strategy) throws InterruptedException;


    /**
     * Releases this lock, granted it has been acquired.
     */
    protected abstract void release();


    /** */
    protected final void invalidate() {
        this.isValid = false;
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
        if (!(o instanceof Lock)) { return false; }
        Lock n = (Lock) o;
        return this.id == n.id;
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
    public final int hashCode() { return this.id; }

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
    public final int compareTo(final Lock lm) {
        if (this.id < lm.id) { return -1; }
        if (this.id > lm.id) { return 1; }
        return 0;
    }
}
