package com.github.undo4j.resources;


/**
 * NullState
 * 
 * @author afs
 * @version 2013
*/

public final class NullState<T> extends AbstractState<T> {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class NullState. */
    public NullState() {}



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    @Override
    public T get() { return null; }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    @Override
    public void set(T value) {
        throw new UnsupportedOperationException("null state");
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    @Override
    public boolean isNull() { return true; }



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public NullState<T> clone() { return new NullState<T>(); }
}
