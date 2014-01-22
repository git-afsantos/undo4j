package com.github.undo4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * ReadWriteLock
 * 
 * @author afs
 * @version 2013
 */

final class ReadWriteLock extends Lock {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    private volatile AccessMode currentMode     = AccessMode.READ;
    private final ReentrantReadWriteLock lock   = new ReentrantReadWriteLock();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class ReadWriteLock.
     */
    ReadWriteLock() { super(); }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    protected IsolationLevel getIsolationLevel() {
        return IsolationLevel.READ_WRITE;
    }


    /** */
    @Override
    protected boolean acquire
        (final AccessMode mode, final LockStrategy strat)
            throws InterruptedException {
        boolean locked = false;
        switch (mode) {
            case READ:
            locked = super.isValid && strat.acquire(lock.readLock());
            break;

            case WRITE:
            locked = super.isValid && strat.acquire(lock.writeLock());
            break;
        }
        // Set current mode, if the lock has been acquired.
        if (locked) { currentMode = mode; }
        return locked;
    }


    /** */
    @Override
    protected void release() {
        // Release the appropriate lock.
        // This should only be called when a lock is held.
        switch (currentMode) {
            case READ:
            assert !lock.writeLock().isHeldByCurrentThread();
            assert lock.getReadLockCount() > 0;
            lock.readLock().unlock();
            return;

            case WRITE:
            assert lock.writeLock().isHeldByCurrentThread();
            lock.writeLock().unlock();
            return;
        }
    }
}
