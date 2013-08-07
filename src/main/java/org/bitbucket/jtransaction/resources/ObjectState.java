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
 * ObjectState
 * 
 * @author afs
 * @version 2013
*/

public final class ObjectState extends NonNullState {
    // instance variables
    private Object value;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class ObjectState. */
    public ObjectState() { value = null; }


    /** Parameter constructor of objects of class ObjectState. */
    public ObjectState(Object o) { value = o; }


    /** Copy constructor of objects of class ObjectState. */
    private ObjectState(ObjectState instance) {
        value = instance.getValue();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    public Object getValue() { return value; }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    public void setValue(Object o) { value = o; }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    // ..



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    // ..



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    // ..



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
        ObjectState n = (ObjectState) o;
        return value.equals(n.getValue());
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
        return 37 + (value != null ? value.hashCode() : 0);
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        return value != null ? value.toString() : "null";
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public ObjectState clone() { return new ObjectState(this); }
}
