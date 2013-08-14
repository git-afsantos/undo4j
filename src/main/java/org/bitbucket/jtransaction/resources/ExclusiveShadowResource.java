package org.bitbucket.jtransaction.resources;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.IsolationLevel;

import static org.bitbucket.jtransaction.common.Check.checkArgument;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ExclusiveShadowResource
 * Does not support read/write locks. Useful for full isolation.
 * 
 * @author afs
 * @version 2013
*/

public final class ExclusiveShadowResource<T> extends ShadowResource<T> {
    private static final String ARGUMENT = "null access mode";

    // instance variables
    private final ReentrantLock lock = new ReentrantLock();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ExclusiveShadowResource. */
    public ExclusiveShadowResource(InternalResource<T> r) { super(r); }


    /** Copy constructor of objects of class ExclusiveShadowResource. */
    private ExclusiveShadowResource(ExclusiveShadowResource<T> instance) {
        super(instance);
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    @Override
    public final InternalResource<T> getSynchronizedResource() {
        InternalResource<T> res = null;
        try {
            this.lock.lock();
            res = getInternalResource();
        } finally { this.lock.unlock(); }
        return res;
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    public boolean isLocked() { return lock.isLocked(); }



    /**************************************************************************
     * Acquirable Interface Methods
    **************************************************************************/

    /** */
    @Override
    public IsolationLevel getIsolationLevel() {
        return IsolationLevel.EXCLUSIVE;
    }


    /** */
    @Override
    public boolean tryAcquireFor(AccessMode mode) {
        checkArgument(ARGUMENT, mode);
        // The mode has no influence on this kind of lock.
        // Try to acquire lock instantly.
        // assert !lock.isHeldByCurrentThread();
        return lock.tryLock();
    }


    /** */
    @Override
    public boolean tryAcquireFor(AccessMode mode, long millis) {
        checkArgument(ARGUMENT, mode);
        // The mode has no influence on this kind of lock.
        // Try to acquire lock within time. Throw exception if impossible.
        // assert !lock.isHeldByCurrentThread();
        try { return lock.tryLock(millis, TimeUnit.MILLISECONDS); }
        catch (InterruptedException ex) {
            throw new ResourceAcquireException(ex.getMessage(), ex);
        }
    }


    /** */
    @Override
    public void acquireFor(AccessMode mode) {
        checkArgument(ARGUMENT, mode);
        // The mode has no influence on this kind of lock.
        // Wait until lock is acquired.
        // assert !lock.isHeldByCurrentThread();
        lock.lock();
    }


    /** */
    @Override
    public void release() {
        // Release the lock. This should only be called when the lock is held.
        // assert lock.isHeldByCurrentThread();
        lock.unlock();
    }



    /**************************************************************************
     * Clone
    **************************************************************************/

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public ExclusiveShadowResource<T> clone() {
        return new ExclusiveShadowResource<T>(this);
    }
}
