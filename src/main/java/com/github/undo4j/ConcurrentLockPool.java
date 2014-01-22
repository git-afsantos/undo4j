package com.github.undo4j;


/**
 * ConcurrentLockPool
 * 
 * @author afs
 * @version 2013
 */

final class ConcurrentLockPool extends LockPool {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** Internal lock. */
    private final Object internalLock = new Object();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class ConcurrentLockPool.
     */
    ConcurrentLockPool() {}



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    @Override
    public Lock getLock(final ResourceId key) {
        assert key != null;
        Lock lock = null;
        synchronized (internalLock) {
            lock = this.locks.get(key);
        }
        if (lock == null) {
            throw new UnregisteredResourceException(key.toString());
        }
        return lock;
    }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    protected void put(final IsolationLevel isolation, final ResourceId key) {
        synchronized (internalLock) {
            super.put(isolation, key);
        }
    }


    /** */
    @Override
    protected void remove(final ResourceId key) {
        synchronized (internalLock) {
            super.remove(key);
        }
    }

    /** */
    @Override
    protected void remove(final Iterable<ResourceId> keys) {
        synchronized (internalLock) {
            super.remove(keys);
        }
    }
}
