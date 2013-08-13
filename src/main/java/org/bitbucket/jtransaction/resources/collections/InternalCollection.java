package org.bitbucket.afsantos.jtransaction.resources.collections;

import org.bitbucket.afsantos.jtransaction.common.Validator;
import org.bitbucket.afsantos.jtransaction.resources.InternalResource;
import org.bitbucket.afsantos.jtransaction.resources.ResourceState;

import static org.bitbucket.afsantos.jtransaction.common.Check.checkArgument;

/**
 * InternalCollection
 * 
 * @author afs
 * @version 2013
*/

abstract class InternalCollection<T> implements InternalResource<T> {
    // instance variables
    private final Validator<ResourceState<T>> validator;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class InternalCollection. */
    protected InternalCollection() {
        this.validator = new NullValidator<T>();
    }


    /** Parameter constructor of objects of class InternalCollection. */
    protected InternalCollection(Validator<ResourceState<T>> val) {
        checkArgument(val);
        this.validator = val;
    }


    /** Copy constructor of objects of class InternalCollection. */
    protected InternalCollection(InternalCollection<T> instance) {
        this.validator = instance.getValidator();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    protected final Validator<ResourceState<T>> getValidator() {
        return this.validator;
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    @Override
    public final boolean isValidState(ResourceState<T> state) {
        return this.validator.isValid(state);
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public void initialize() {}

    /** */
    @Override
    public void dispose() {}



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    protected final void checkValidState(ResourceState<T> state) {
        checkArgument(this.validator.isValid(state));
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
        if (!(o instanceof InternalCollection)) return false;
        InternalCollection<?> n = (InternalCollection<?>) o;
        return this.validator.equals(n.getValidator());
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
        return 37 + this.validator.hashCode();
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.validator);
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public abstract InternalCollection<T> clone();



    /**************************************************************************
     * Nested Classes
    **************************************************************************/

    /** */
    static final class NullValidator<T>
            implements Validator<ResourceState<T>> {
        /** Constructor */
        NullValidator() {}

        /** */
        @Override
        public boolean isValid(ResourceState<T> state) { return true; }
    }
}
