package com.github.undo4j.functional;

import com.github.undo4j.common.Copyable;

import java.util.concurrent.Callable;

/**
 * ComposedCallable
 * 
 * @author afs
 * @version 2013
*/

final class ComposedCallable<A, B>
        implements Callable<B>, Copyable<ComposedCallable<A, B>> {
    // instance variables
    private final Callable<A> callable;
    private final Function<A, B> function;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ComposedCallable. */
    ComposedCallable(Callable<A> c, Function<A, B> f) {
        assert c != null && f != null;
        callable = c;
        function = f;
    }


    /** Copy constructor of objects of class ComposedCallable. */
    private ComposedCallable(ComposedCallable<A, B> instance) {
        this(instance.getCallable(), instance.getFunction());
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    Callable<A> getCallable() { return callable; }

    /** */
    Function<A, B> getFunction() { return function; }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    // ...



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public B call() throws Exception {
        return function.call(callable.call());
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
        if (!(o instanceof ComposedCallable)) return false;
        ComposedCallable<?, ?> n = (ComposedCallable<?, ?>) o;
        return (callable.equals(n.getCallable()) && function.equals(n.getFunction()));
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
        return 37 * (37 + callable.hashCode()) + function.hashCode();
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(function);
        sb.append('.');
        sb.append(callable);
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public ComposedCallable<A, B> clone() {
        return new ComposedCallable<A, B>(this);
    }
}
