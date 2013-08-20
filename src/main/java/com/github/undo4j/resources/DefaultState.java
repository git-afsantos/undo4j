package com.github.undo4j.resources;


/**
 * DefaultState
 * 
 * @author afs
 * @version 2013
*/

public final class DefaultState<T> extends AbstractState<T> {
    // instance variables
    private final T defaultValue;
    private T value;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class DefaultState. */
    public DefaultState(T val) {
        defaultValue = null;
        value = val;
    }

    /** Parameter constructor of objects of class DefaultState. */
    public DefaultState(T def, T val) {
        defaultValue = def;
        value = val;
    }


    /** Copy constructor of objects of class DefaultState. */
    private DefaultState(DefaultState<? extends T> instance) {
        defaultValue = instance.getDefault();
        value = instance.get();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    @Override
    public T get() { return value; }

    /** */
    private T getDefault() { return defaultValue; }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    @Override
    public void set(T val) {
        if (val == null) { value = defaultValue; }
        else { value = val; }
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    @Override
    public boolean isNull() { return value == null; }



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public DefaultState<T> clone() { return new DefaultState<T>(this); }
}
