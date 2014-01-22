package com.github.undo4j;

import com.github.undo4j.AcquireStrategy;

import java.util.concurrent.locks.Lock;


/**
 * BlockingStrategy
 * 
 * @author afs
 * @version 2013
 */

final class BlockingStrategy extends LockStrategy {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class BlockingStrategy.
     */
    BlockingStrategy() { super(); }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    protected AcquireStrategy getAcquireStrategy() {
        return AcquireStrategy.BLOCKING;
    }


    /**
     * Acquires the lock, blocking if necessary. Returns true if the lock has
     * been acquired. Returns false otherwise.
     */
    @Override
    protected boolean acquire(final Lock lock) {
        lock.lock();
        return true;
    }
}
