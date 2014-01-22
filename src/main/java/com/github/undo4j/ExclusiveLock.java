package com.github.undo4j;

import java.util.concurrent.locks.ReentrantLock;


/**
 * ExclusiveLock
 * 
 * @author afs
 * @version 2013
 */

final class ExclusiveLock extends Lock {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    private final ReentrantLock lock = new ReentrantLock();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class ExclusiveLock.
     */
    ExclusiveLock() { super(); }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    protected IsolationLevel getIsolationLevel() {
        return IsolationLevel.EXCLUSIVE;
    }


    /** */
    @Override
    protected boolean acquire
        (final AccessMode mode, final LockStrategy strat)
            throws InterruptedException {
        return super.isValid && strat.acquire(lock);
    }


    /** */
    @Override
    protected void release() { lock.unlock(); }
}
