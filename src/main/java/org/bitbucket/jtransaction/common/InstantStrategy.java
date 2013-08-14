package org.bitbucket.jtransaction.common;

import java.util.concurrent.locks.Lock;

/**
 * InstantStrategy
 * 
 * @author afs
 * @version 2013
*/

final class InstantStrategy extends LockStrategy {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /**
     * Empty constructor of objects of class InstantStrategy.
     */
    InstantStrategy() {}


    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /**
     * Tries to instantly acquire the lock.
     * Returns true if the lock has been acquired. Returns false otherwise.
     */
    @Override
    protected boolean acquire(Lock lock) { return lock.tryLock(); }
}
