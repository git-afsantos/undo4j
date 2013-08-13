package org.bitbucket.jtransaction.functional;

import org.bitbucket.jtransaction.common.Copyable;

/**
 * ComposedFunction
 * 
 * @author afs
 * @version 2013
*/

final class ComposedFunction<A, B, C>
        implements Function<A, C>, Copyable<ComposedFunction<A, B, C>> {
    // instance variables
    private final Function<A, B> first;
    private final Function<B, C> second;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ComposedFunction. */
    ComposedFunction(Function<A, B> f, Function<B, C> g) {
        assert f != null && g != null;
        first = f;
        second = g;
    }


    /** Copy constructor of objects of class ComposedFunction. */
    private ComposedFunction(ComposedFunction<A, B, C> instance) {
        this(instance.getFirst(), instance.getSecond());
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    Function<A, B> getFirst() { return first; }

    /** */
    Function<B, C> getSecond() { return second; }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    // ...



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public C call(A a) throws Exception {
        return second.call(first.call(a));
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
        if (!(o instanceof ComposedFunction)) return false;
        ComposedFunction<?, ?, ?> n = (ComposedFunction<?, ?, ?>) o;
        return (first.equals(n.getFirst()) && second.equals(n.getSecond()));
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
        return 37 * (37 + first.hashCode()) + second.hashCode();
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(second);
        sb.append('.');
        sb.append(first);
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public ComposedFunction<A, B, C> clone() {
        return new ComposedFunction<A, B, C>(this);
    }
}
