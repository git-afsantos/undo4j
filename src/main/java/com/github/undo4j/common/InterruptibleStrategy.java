package com.github.undo4j.common;

import java.util.concurrent.locks.Lock;

/**
 * InterruptibleStrategy
 * 
 * @author afs
 * @version 2013
*/

final class InterruptibleStrategy extends LockStrategy {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /**
     * Empty constructor of objects of class InterruptibleStrategy.
     */
    InterruptibleStrategy() {}


    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /**
     * Acquires the lock, blocking if necessary.
     * Throws InterruptedException if the thread is interrupted while waiting.
     * Returns true when the lock has been acquired.
     */
    @Override
    protected boolean acquire(Lock lock) throws InterruptedException {
        lock.lockInterruptibly();
        return true;
    }
}
