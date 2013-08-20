package com.github.undo4j.functional;

import java.util.concurrent.Callable;

/**
 * CallableFunction
 * 
 * @author afs
 * @version 2013
*/

final class CallableFunction<A, B> implements Callable<B> {
    // instance variables
    private final A argument;
    private final Function<A, B> function;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class CallableFunction. */
    public CallableFunction(Function<A, B> f, A arg) {
        assert f != null && arg != null;
        function = f;
        argument = arg;
    }


    /** Copy constructor of objects of class CallableFunction. */
    private CallableFunction(CallableFunction<A, B> instance) {
        function = instance.getFunction();
        argument = instance.getArgument();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    A getArgument() { return argument; }

    /** */
    Function<A, B> getFunction() { return function; }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public B call() throws Exception {
        return function.call(argument);
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
        CallableFunction<?, ?> n = (CallableFunction<?, ?>) o;
        return function.equals(n.getFunction()) &&
                argument.equals(n.getArgument());
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
        return 37 * function.hashCode() + argument.hashCode();
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(function);
        sb.append(argument);
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public CallableFunction<A, B> clone() {
        return new CallableFunction<A, B>(this);
    }
}
