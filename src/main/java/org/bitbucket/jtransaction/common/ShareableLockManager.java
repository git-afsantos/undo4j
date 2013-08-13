package org.bitbucket.jtransaction.common;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ShareableLockManager
 * 
 * @author afs
 * @version 2013
*/

final class ShareableLockManager extends LockManager {
    // instance variables
    private final ReentrantLock lock;
    private final LockStrategy strategy;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ShareableLockManager. */
    ShareableLockManager(LockStrategy lockStrategy) {
        super(IsolationLevel.READ_COMMITTED);
        lock = new ReentrantLock();
        strategy = lockStrategy;
    }


    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public boolean acquire(AccessMode mode) throws InterruptedException {
        if (mode == AccessMode.WRITE) { return strategy.acquire(lock); }
        return mode == AccessMode.READ;
    }

    /** */
    @Override
    public void release() {
        if (lock.isHeldByCurrentThread()) { lock.unlock(); }
    }
}
