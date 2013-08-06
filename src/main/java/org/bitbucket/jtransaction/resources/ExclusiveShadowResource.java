/*
The MIT License (MIT)

Copyright (c) 2013 Andre Santos, Victor Miraldo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/


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

public final class ExclusiveShadowResource extends ShadowResource {
    private static final String ARGUMENT = "null access mode";

    // instance variables
    private final ReentrantLock lock = new ReentrantLock();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ExclusiveShadowResource. */
    public ExclusiveShadowResource(InternalResource r) { super(r); }


    /** Copy constructor of objects of class ExclusiveShadowResource. */
    private ExclusiveShadowResource(ExclusiveShadowResource instance) {
        super(instance);
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    @Override
    public final InternalResource getSynchronizedResource() {
        InternalResource res = null;
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
    public ExclusiveShadowResource clone() {
        return new ExclusiveShadowResource(this);
    }
}
