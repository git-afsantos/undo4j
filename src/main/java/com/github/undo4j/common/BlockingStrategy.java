package com.github.undo4j.common;

import java.util.concurrent.locks.Lock;

/**
 * BlockingStrategy
 * 
 * @author afs
 * @version 2013
*/

final class BlockingStrategy extends LockStrategy {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /**
     * Empty constructor of objects of class BlockingStrategy.
     */
    BlockingStrategy() {}


    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /**
     * Acquires the lock, blocking if necessary.
     * Returns true if the lock has been acquired. Returns false otherwise.
     */
    @Override
    protected boolean acquire(Lock lock) {
        lock.lock();
        return true;
    }
}
