package com.github.undo4j.counter;

/**
 * Counter
 * 
 * @author afs
 * @version 2013
 */

public class Counter {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    private int value = 0;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class Counter.
     */
    public Counter() {}



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public void increment() { ++value; }

    /** */
    public void decrement() { --value; }

    /** */
    public int get() { return value; }


    /** */
    @Override
    public String toString() { return Integer.toString(value); }
}
