package com.github.undo4j;

import java.util.concurrent.locks.Condition;


/**
 * WaitStrategy
 * 
 * @author afs
 * @version 2013
 */

abstract class WaitStrategy {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class WaitStrategy.
     */
    protected WaitStrategy() {}



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    protected abstract WaitMethod getWaitMethod();


    /** */
    protected abstract boolean waitOn(final Condition condition)
        throws InterruptedException;
}
