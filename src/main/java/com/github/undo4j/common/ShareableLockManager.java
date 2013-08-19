package com.github.undo4j.common;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ShareableLockManager
 * 
 * @author afs
 * @version 2013
*/

final class ShareableLockManager extends StrategizedLockManager {
    // instance variables
    private final ReentrantLock lock;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ShareableLockManager. */
    ShareableLockManager(LockStrategy lockStrategy) {
        super(IsolationLevel.READ_COMMITTED, lockStrategy);
        lock = new ReentrantLock();
    }


    /** Copy constructor of objects of class ShareableLockManager. */
    ShareableLockManager(ShareableLockManager instance) {
        super(instance);
        lock = new ReentrantLock();
    }


    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public boolean acquire(AccessMode mode) throws InterruptedException {
        if (mode == AccessMode.WRITE) {
        	return getLockStrategy().acquire(lock);
        }
        return mode == AccessMode.READ;
    }

    /** */
    @Override
    public void release() {
        if (lock.isHeldByCurrentThread()) { lock.unlock(); }
    }


    /** */
    @Override
    public ShareableLockManager clone() {
    	return new ShareableLockManager(this);
    }
}
