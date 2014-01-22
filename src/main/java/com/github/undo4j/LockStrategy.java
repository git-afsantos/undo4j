package com.github.undo4j;

import com.github.undo4j.AcquireStrategy;

import java.util.concurrent.locks.Lock;


/**
 * LockStrategy
 * 
 * @author afs
 * @version 2013
 */

abstract class LockStrategy {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class LockStrategy.
     */
    protected LockStrategy() {}



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    protected abstract AcquireStrategy getAcquireStrategy();


    /** */
    protected abstract boolean acquire(final Lock lock)
        throws InterruptedException;
}
