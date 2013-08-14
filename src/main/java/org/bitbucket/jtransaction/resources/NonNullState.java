package org.bitbucket.jtransaction.resources;

import static org.bitbucket.jtransaction.common.Check.checkArgument;

/**
 * NonNullState
 * 
 * @author afs
 * @version 2013
*/

public final class NonNullState<T> extends AbstractState<T> {
    // instance variables
    private T value;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class NonNullState. */
    public NonNullState(T val) {
        checkArgument(val);
        value = val;
    }


    /** Copy constructor of objects of class NonNullState. */
    private NonNullState(NonNullState<? extends T> instance) {
        value = instance.get();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    @Override
    public T get() { return value; }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    @Override
    public void set(T val) {
        checkArgument(val);
        value = val;
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    @Override
    public boolean isNull() { return false; }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    // ...



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    // ...



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public NonNullState<T> clone() { return new NonNullState<T>(this); }
}
