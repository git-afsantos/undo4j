package org.bitbucket.jtransaction.resources;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.IsolationLevel;

import static org.bitbucket.jtransaction.common.Check.checkArgument;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ShareableShadowResource
 * Allows readers operating on the last commit, and one writer operating
 * on the current state, simultaneously.
 * 
 * @author afs
 * @version 2013
*/

public final class ShareableShadowResource<T> extends ShadowResource<T> {
    private static final String ARGUMENT = "null access mode";

    // instance variables
    private final ReentrantLock lock = new ReentrantLock();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ShareableShadowResource. */
    public ShareableShadowResource(InternalResource<T> r) { super(r); }


    /** Copy constructor of objects of class ShareableShadowResource. */
    private ShareableShadowResource(ShareableShadowResource<T> instance) {
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
     * Acquirable Interface Methods
    **************************************************************************/

    /** */
    @Override
    public IsolationLevel getIsolationLevel() {
        return IsolationLevel.READ_COMMITTED;
    }


    /** */
    @Override
    public boolean tryAcquireFor(AccessMode mode) {
        checkArgument(ARGUMENT, mode);
        // The lock is only acquired for writing.
        // assert !lock.isHeldByCurrentThread();
        if (mode == AccessMode.WRITE) { return lock.tryLock(); }
        return true;
    }


    /** */
    @Override
    public boolean tryAcquireFor(AccessMode mode, long millis) {
        checkArgument(ARGUMENT, mode);
        // The lock is only acquired for writing.
        // assert !lock.isHeldByCurrentThread();
        if (mode == AccessMode.WRITE) {
            try { return lock.tryLock(millis, TimeUnit.MILLISECONDS); }
            catch (InterruptedException ex) {
                throw new ResourceAcquireException(ex.getMessage(), ex);
            }
        }
        return true;
    }


    /** */
    @Override
    public void acquireFor(AccessMode mode) {
        checkArgument(ARGUMENT, mode);
        // The lock is only acquired for writing.
        // Throw exception if impossible to acquire lock for writing.
        // assert !lock.isHeldByCurrentThread();
        if (mode == AccessMode.WRITE) { lock.lock(); }
    }


    /** */
    @Override
    public void release() {
        // Release the lock. This should only be called when the lock is held.
        // Using conditional instead of assertion.
        // Readers will call this method, but will never have a lock.
        if (lock.isHeldByCurrentThread()) { lock.unlock(); }
    }



    /**************************************************************************
     * Clone
    **************************************************************************/

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public ShareableShadowResource<T> clone() {
        return new ShareableShadowResource<T>(this);
    }
}
