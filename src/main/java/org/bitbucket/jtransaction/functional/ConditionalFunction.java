package org.bitbucket.jtransaction.functional;

import org.bitbucket.jtransaction.common.Copyable;

/**
 * ConditionalFunction
 * 
 * @author afs
 * @version 2013
*/

final class ConditionalFunction<A, B>
        implements Function<A, B>, Copyable<ConditionalFunction<A, B>> {
    // instance variables
    private final Predicate predicate;
    private final Function<A, B> functionThen;
    private final Function<A, B> functionElse;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ConditionalFunction. */
    ConditionalFunction(
        Predicate p, Function<A, B> cThen, Function<A, B> cElse
    ) {
        assert p != null && cThen != null && cElse != null;
        predicate = p;
        functionThen = cThen;
        functionElse = cElse;
    }


    /** Copy constructor of objects of class ConditionalFunction. */
    private ConditionalFunction(ConditionalFunction<A, B> instance) {
        this(
            instance.getPredicate(),
            instance.getThen(), instance.getElse()
        );
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    Predicate getPredicate() { return predicate; }

    /** */
    Function<A, B> getThen() { return functionThen; }

    /** */
    Function<A, B> getElse() { return functionElse; }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    // ...



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public B call(A arg) throws Exception {
        if (predicate.evaluate()) {
            return functionThen.call(arg);
        } else {
            return functionElse.call(arg);
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
        ConditionalFunction<?, ?> n = (ConditionalFunction<?, ?>) o;
        return (predicate.equals(n.getPredicate()) &&
                functionThen.equals(n.getThen()) &&
                functionElse.equals(n.getElse()));
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
        hash = 37 * hash + functionThen.hashCode();
        return 37 * hash + functionElse.hashCode();
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
    public ConditionalFunction<A, B> clone() {
        return new ConditionalFunction<A, B>(this);
    }
}
