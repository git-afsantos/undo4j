/*
The MIT License (MIT)

Copyright (c) 2013 Andre Santos, Victor Miraldo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/


package org.bitbucket.jtransaction.resources;

/**
 * IntState
 * 
 * @author afs
 * @version 2013
*/

public final class IntState extends NonNullState {
    // instance variables
    private int value;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class IntState. */
    public IntState() {
        value = 0;
    }


    /** Parameter constructor of objects of class IntState. */
    public IntState(int x) {
        value = x;
    }


    /** Copy constructor of objects of class IntState. */
    private IntState(IntState instance) {
        value = instance.getValue();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    public int getValue() { return value; }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    public void setValue(int x) { value = x; }



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Equivalence relation. Contract (for any non-null reference values
            value, y, and z):
        * Reflevalueive: value.equals(value).
        * Symmetric: value.equals(y) iff y.equals(value).
        * Transitive: if value.equals(y) and y.equals(z), then value.equals(z).
        * Consistency: successive calls (with no modification of the equality
            fields) return the same result.
        * value.equals(null) should return false.
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        IntState n = (IntState) o;
        return (value == n.getValue());
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
        return value;
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public IntState clone() { return new IntState(this); }
}
