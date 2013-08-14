package org.bitbucket.jtransaction.resources;


/**
 * ImmutableState
 * 
 * @author afs
 * @version 2013
*/

public final class ImmutableState<T> extends AbstractState<T> {
    // instance variables
    private final T value;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ImmutableState. */
    public ImmutableState(T val) {
        value = val;
    }


    /** Copy constructor of objects of class ImmutableState. */
    private ImmutableState(ImmutableState<? extends T> instance) {
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
        throw new UnsupportedOperationException("immutable state");
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    @Override
    public boolean isNull() { return value == null; }



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
    public ImmutableState<T> clone() { return new ImmutableState<T>(this); }
}
