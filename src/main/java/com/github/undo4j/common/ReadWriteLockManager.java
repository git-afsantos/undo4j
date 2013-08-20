package com.github.undo4j.common;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLockManager
 * 
 * @author afs
 * @version 2013
*/

final class ReadWriteLockManager extends StrategizedLockManager {
    // instance variables
    private volatile AccessMode currentMode;
    private final ReentrantReadWriteLock lock;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ReadWriteLockManager. */
    ReadWriteLockManager(LockStrategy lockStrategy) {
        super(IsolationLevel.READ_WRITE, lockStrategy);
        currentMode = AccessMode.READ;
        lock = new ReentrantReadWriteLock();
    }


    /** Copy constructor of objects of class ReadWriteLockManager. */
    ReadWriteLockManager(ReadWriteLockManager instance) {
        super(instance);
        currentMode = AccessMode.READ;
        lock = new ReentrantReadWriteLock();
    }


    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public boolean acquire(AccessMode mode) throws InterruptedException {
        boolean locked = false;
        switch (mode) {
            case READ:
            locked = getLockStrategy().acquire(lock.readLock());
            break;

            case WRITE:
            locked = getLockStrategy().acquire(lock.writeLock());
            break;
        }
        // Set current mode, if the lock has been acquired.
        if (locked) { currentMode = mode; }
        return locked;
    }

    /** */
    @Override
    public void release() {
        // Release the appropriate lock.
        // This should only be called when a lock is held.
        switch (currentMode) {
            case READ:
            // assert !lock.writeLock().isHeldByCurrentThread();
            // assert lock.getReadLockCount() > 0;
            lock.readLock().unlock();
            return;

            case WRITE:
            // assert lock.writeLock().isHeldByCurrentThread();
            lock.writeLock().unlock();
            return;
        }
    }


    /** */
    @Override
    public ReadWriteLockManager clone() {
		return new ReadWriteLockManager(this);
    }
}
