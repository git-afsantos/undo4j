package com.github.undo4j.resources;


/**
 * AbstractState
 * 
 * @author afs
 * @version 2013
*/

public abstract class AbstractState<T> implements ResourceState<T> {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class AbstractState. */
    protected AbstractState() {}



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
        if (!(o instanceof ResourceState)) return false;
        ResourceState<?> n = (ResourceState<?>) o;
        T value = get();
        if (value == null) { return n.get() == null; }
        return value.equals(n.get());
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
        T value = get();
        return (value == null ? 37 : value.hashCode());
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() { return String.valueOf(get()); }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public abstract AbstractState<T> clone();
}
