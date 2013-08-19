package com.github.undo4j.common;


/**
 * LockManager
 * 
 * @author afs
 * @version 2013
*/

public abstract class LockManager implements Acquirable, Copyable<LockManager> {
    // instance variables
    private final IsolationLevel isolation;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class LockManager. */
    protected LockManager(IsolationLevel isolationLevel) {
        this.isolation = isolationLevel;
    }

    /** Copy constructor of objects of class LockManager. */
    protected LockManager(LockManager instance) {
        this.isolation = instance.getIsolationLevel();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    @Override
    public final IsolationLevel getIsolationLevel() { return this.isolation; }



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
        if (!(o instanceof LockManager)) return false;
        LockManager n = (LockManager) o;
        return (this.isolation == n.getIsolationLevel());
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
    public int hashCode() { return this.isolation.hashCode(); }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.isolation);
        return sb.toString();
    }


    /** */
    @Override
    public abstract LockManager clone();
}
