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
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteShadowResource
 * 
 * @author afs
 * @version 2013
*/

public final class ReadWriteShadowResource extends ShadowResource {
    private static final String ARGUMENT = "null access mode";

    // instance variables
    private volatile AccessMode currentMode = AccessMode.READ;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ReadWriteShadowResource. */
    public ReadWriteShadowResource(InternalResource r) { super(r); }


    /** Copy constructor of objects of class ReadWriteShadowResource. */
    private ReadWriteShadowResource(ReadWriteShadowResource instance) {
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
            this.lock.readLock().lock();
            res = getInternalResource();
        } finally { this.lock.readLock().unlock(); }
        return res;
    }



    /**************************************************************************
     * Acquirable Interface Methods
    **************************************************************************/

    /** */
    @Override
    public IsolationLevel getIsolationLevel() {
        return IsolationLevel.READ_WRITE;
    }


    /** */
    @Override
    public boolean tryAcquireFor(AccessMode mode) {
        checkArgument(ARGUMENT, mode);
        // Try to acquire the appropriate lock.
        // Throw exception if impossible to acquire lock.
        // assert !lock.writeLock().isHeldByCurrentThread();
        boolean locked = false;
        switch (mode) {
            case READ:
            locked = lock.readLock().tryLock();
            break;

            case WRITE:
            locked = lock.writeLock().tryLock();
            break;
        }
        // Set current mode, if the lock has been acquired.
        if (locked) { currentMode = mode; }
        return locked;
    }


    /** */
    @Override
    public boolean tryAcquireFor(AccessMode mode, long mil) {
        checkArgument(ARGUMENT, mode);
        // Try to acquire the appropriate lock.
        // Throw exception if impossible to acquire lock.
        // assert !lock.writeLock().isHeldByCurrentThread();
        boolean locked = false;
        switch (mode) {
            case READ:
            try {
                locked = lock.readLock().tryLock(mil, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new ResourceAcquireException(e.getMessage(), e);
            }
            break;

            case WRITE:
            try {
                locked = lock.writeLock().tryLock(mil, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new ResourceAcquireException(e.getMessage(), e);
            }
            break;
        }
        // Set current mode, if the lock has been acquired.
        if (locked) { currentMode = mode; }
        return locked;
    }


    /** */
    @Override
    public void acquireFor(AccessMode mode) {
        checkArgument(ARGUMENT, mode);
        // Try to acquire the appropriate lock.
        // Throw exception if impossible to acquire lock.
        // assert !lock.writeLock().isHeldByCurrentThread();
        switch (mode) {
            case READ:
            lock.readLock().lock();
            currentMode = mode;
            break;

            case WRITE:
            lock.writeLock().lock();
            currentMode = mode;
            break;
        }
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



    /**************************************************************************
     * Clone
    **************************************************************************/

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public ReadWriteShadowResource clone() {
        return new ReadWriteShadowResource(this);
    }
}
