package com.github.undo4j;

import java.util.HashMap;
import java.util.Map;


/**
 * LockPool
 * 
 * @author afs
 * @version 2013
 */

class LockPool implements LockProvider {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** Registry of resource locks. */
    protected final Map<ResourceId, Lock> locks = new HashMap<>();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class LockPool.
     */
    LockPool() {}



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    @Override
    public Lock getLock(final ResourceId key) {
        assert key != null;
        Lock lock = this.locks.get(key);
        if (lock == null) {
            throw new UnregisteredResourceException(key.toString());
        }
        return lock;
    }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    void putReadWrite(final ResourceId key) {
        this.put(IsolationLevel.READ_WRITE, key);
    }


    /** */
    void putExclusive(final ResourceId key) {
        this.put(IsolationLevel.EXCLUSIVE, key);
    }


    /** */
    void putNoLock(final ResourceId key) {
        this.put(IsolationLevel.NONE, key);
    }


    /** */
    protected void put(final IsolationLevel isolation, final ResourceId key) {
        assert isolation != null;
        checkNotRegistered(key);
        this.locks.put(key, Locks.newLock(isolation));
    }


    /** */
    protected void remove(final ResourceId key) {
        Lock lock = this.locks.remove(key);
        if (lock != null) {
            lock.invalidate();
        }
    }

    /** */
    void remove(final Iterable<ResourceId> keys) {
        for (ResourceId key: keys) {
            Lock lock = this.locks.remove(key);
            if (lock != null) {
                lock.invalidate();
            }
        }
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */
    private void checkNotRegistered(final ResourceId key) {
        if (this.locks.containsKey(key)) {
            throw new RegisteredResourceException(key.toString());
        }
    }



    /*************************************************************************\
     *  Equals, HashCode, ToString & Clone
    \*************************************************************************/

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return this.locks.toString();
    }
}
