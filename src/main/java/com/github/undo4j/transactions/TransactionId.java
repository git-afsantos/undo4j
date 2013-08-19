package com.github.undo4j.transactions;

import com.github.undo4j.common.Copyable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TransactionId
 * 
 * @author afs
 * @version 2013
*/

public final class TransactionId implements Copyable<TransactionId> {
    // Atomic integer containing the next transaction ID to be assigned.
    private static final AtomicInteger NEXT_ID = new AtomicInteger();

    // instance variables
    private final int id;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class TransactionId. */
    private TransactionId(int id) { this.id = id; }


    /** Copy constructor of objects of class TransactionId. */
    private TransactionId(TransactionId instance) { this(instance.getId()); }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    private int getId() { return id; }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** Creates and returns a fresh transaction identifier.
     */
    static TransactionId newTransactionId() {
        return new TransactionId(NEXT_ID.incrementAndGet());
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
        if (o == null || this.getClass() != o.getClass()) return false;
        TransactionId n = (TransactionId) o;
        return id == n.getId();
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
    public int hashCode() { return id; }


    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        return sb.toString();
    }


    /** Creates and returns a (deep) copy of this object. */
    @Override
    public TransactionId clone() { return new TransactionId(this); }
}
