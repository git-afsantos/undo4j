package org.bitbucket.jtransaction.common;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ExclusiveLockManager
 * 
 * @author afs
 * @version 2013
*/

final class ExclusiveLockManager extends LockManager {
    // instance variables
    private final ReentrantLock lock;
    private final LockStrategy strategy;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ExclusiveLockManager. */
    ExclusiveLockManager(LockStrategy lockStrategy) {
        super(IsolationLevel.EXCLUSIVE);
        lock = new ReentrantLock();
        strategy = lockStrategy;
    }


    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public boolean acquire(AccessMode mode) throws InterruptedException {
        return strategy.acquire(lock);
    }

    /** */
    @Override
    public void release() { lock.unlock(); }
}
