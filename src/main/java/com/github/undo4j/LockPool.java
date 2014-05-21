package com.github.undo4j;

import java.util.HashMap;
import java.util.Map;


/**
 * LockPool
 * 
 * @author afs
 * @version 2013
 */

class LockPool implements ResourceManager {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** Registry of resource locks. */
    protected final Map<ResourceId, ResourceLock> locks = new HashMap<>();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class LockPool.
     */
    LockPool() {}



    /*************************************************************************\
     *  ResourceManager Methods
    \*************************************************************************/

    /** */
    @Override
    public void acquire(
            final TransactionId tid,
            final ResourceId key,
            final AccessMode mode,
            final WaitStrategy strategy) {
        assert tid != null && key != null && mode != null && strategy != null;
        checkRegistered(key);
        try {
            if (!this.locks.get(key).acquire(tid, mode, strategy)) {
                throw new ResourceAcquisitionException("Failed to acquire");
            }
        } catch (InterruptedException ex) {
            throw new TransactionInterruptedException(ex);
        }
    }


    /** */
    @Override
    public void release(final TransactionId tid, final ResourceId key) {
        assert tid != null && key != null;
        checkRegistered(key);
        this.locks.get(key).release(tid);
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
        this.locks.put(key, ResourceLocks.noLock(key));
    }


    /** */
    protected void remove(final ResourceId key) {
        ResourceLock lock = this.locks.remove(key);
        if (lock != null) { lock.invalidate(); }
    }

    /** */
    protected void remove(final Iterable<ResourceId> keys) {
        for (ResourceId key: keys) {
            ResourceLock lock = this.locks.remove(key);
            if (lock != null) { lock.invalidate(); }
        }
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */
    protected final void checkNotRegistered(final ResourceId key) {
        if (this.locks.containsKey(key)) {
            throw new RegisteredResourceException(key.toString());
        }
    }

    /** */
    protected final void checkRegistered(final ResourceId key) {
        if (!this.locks.containsKey(key)) {
            throw new UnregisteredResourceException(key.toString());
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
