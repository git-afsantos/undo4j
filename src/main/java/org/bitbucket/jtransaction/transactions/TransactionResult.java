package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.Copyable;

/**
 * TransactionResult
 * 
 * @author afs
 * @version 2013
*/

public final class TransactionResult<T>
        implements Copyable<TransactionResult<T>> {
    // instance variables
    private final T result;
    private final TransactionStatistics stats;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class TransactionResult. */
    TransactionResult() { this(null, new TransactionStatistics()); }


    /** Parameter constructor of objects of class TransactionResult. */
    TransactionResult(T res, TransactionStatistics tStats) {
        result = res;
        stats = tStats;
    }


    /** Copy constructor of objects of class TransactionResult. */
    private TransactionResult(TransactionResult<T> instance) {
        this(instance.getResult(), instance.getStatistics());
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    public T getResult() { return result; }

    /** */
    public TransactionStatistics getStatistics() { return stats.clone(); }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    public boolean hasResult() { return result != null; }



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
        TransactionResult n = (TransactionResult) o;
        return result.equals(n.getResult());
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
        return 37 + (result != null ? result.hashCode() : 0);
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(result);
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public TransactionResult<T> clone() { return new TransactionResult<T>(this); }
}
