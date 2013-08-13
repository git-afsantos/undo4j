package org.bitbucket.jtransaction.functional;

import org.bitbucket.jtransaction.common.Copyable;

import java.util.concurrent.Callable;

/**
 * ConditionalCallable
 * 
 * @author afs
 * @version 2013
*/

final class ConditionalCallable<T>
        implements Callable<T>, Copyable<ConditionalCallable<T>> {
    // instance variables
    private final Predicate predicate;
    private final Callable<T> callableThen;
    private final Callable<T> callableElse;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ConditionalCallable. */
    ConditionalCallable(Predicate p, Callable<T> cThen, Callable<T> cElse) {
        assert p != null && cThen != null && cElse != null;
        predicate = p;
        callableThen = cThen;
        callableElse = cElse;
    }


    /** Copy constructor of objects of class ConditionalCallable. */
    private ConditionalCallable(ConditionalCallable<T> instance) {
        this(instance.getPredicate(), instance.getThen(), instance.getElse());
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    Predicate getPredicate() { return predicate; }

    /** */
    Callable<T> getThen() { return callableThen; }

    /** */
    Callable<T> getElse() { return callableElse; }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    // ...



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public T call() throws Exception {
        if (predicate.evaluate()) {
            return callableThen.call();
        } else {
            return callableElse.call();
        }
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    // ...



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
        ConditionalCallable<?> n = (ConditionalCallable<?>) o;
        return (predicate.equals(n.getPredicate()) &&
                callableThen.equals(n.getThen()) &&
                callableElse.equals(n.getElse()));
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
        int hash = 37 + predicate.hashCode();
        hash = 37 * hash + callableThen.hashCode();
        return 37 * hash + callableElse.hashCode();
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(predicate);
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public ConditionalCallable<T> clone() {
        return new ConditionalCallable<T>(this);
    }
}
