package com.github.undo4j;

import com.github.undo4j.AcquireStrategy;

import java.util.concurrent.locks.Lock;


/**
 * InstantStrategy
 * 
 * @author afs
 * @version 2013
 */

final class InstantStrategy extends LockStrategy {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class InstantStrategy.
     */
    InstantStrategy() { super(); }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    protected AcquireStrategy getAcquireStrategy() {
        return AcquireStrategy.INSTANT;
    }


    /**
     * Tries to instantly acquire the lock. Returns true if the lock has been
     * acquired. Returns false otherwise.
     */
    @Override
    protected boolean acquire(final Lock lock) { return lock.tryLock(); }
}
