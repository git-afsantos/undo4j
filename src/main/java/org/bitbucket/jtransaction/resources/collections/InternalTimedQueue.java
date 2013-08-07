/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Andre Santos, Victor Miraldo
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.bitbucket.jtransaction.resources.collections;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.bitbucket.jtransaction.common.Validator;
import org.bitbucket.jtransaction.resources.ResourceState;

/**
 * InternalTimedQueue
 * 
 * @author afs
 * @version 2013
*/

final class InternalTimedQueue extends InternalConcurrentQueue {
    private static final long DEFAULT_WAIT = 1000L;

    // instance variables
    private final long wait;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue() {
        this(Integer.MAX_VALUE, DEFAULT_WAIT);
    }

    /** Parameter constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue(int cap) {
        this(cap, DEFAULT_WAIT);
    }

    /** Parameter constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue(long maxWait) {
        this(Integer.MAX_VALUE, maxWait);
    }

    /** Parameter constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue(int cap, long maxWait) {
        super(cap, new LinkedBlockingQueue<ResourceState>(cap));
        wait = maxWait;
    }

    /** Parameter constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue(int cap, long maxWait, Validator<ResourceState> val) {
        super(cap, new LinkedBlockingQueue<ResourceState>(cap), val);
        wait = maxWait;
    }

    /** Copy constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue(InternalTimedQueue instance) {
        super(instance);
        wait = instance.getWaitTime();
    }

    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    long getWaitTime() {
        return wait;
    }

    /** */
    @Override
    protected BlockingQueue<ResourceState> getQueue() {
        int cap = getCapacity();
        BlockingQueue<ResourceState> queue = getQueueReference();
        BlockingQueue<ResourceState> copy = new LinkedBlockingQueue<ResourceState>(cap);
        for (ResourceState s : queue) {
            copy.offer(s.clone());
        }
        return copy;
    }

    /**************************************************************************
     * Predicates
    **************************************************************************/

    // ..

    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    public ResourceState buildState() throws InterruptedException {
        return getQueueReference().poll(wait, TimeUnit.MILLISECONDS);
    }

    /** */
    public void applyState(ResourceState state) throws InterruptedException {
        checkValidState(state);
        getQueueReference().offer(state, wait, TimeUnit.MILLISECONDS);
    }

    /**************************************************************************
     * Private Methods
    **************************************************************************/

    // ..

    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Returns a copy of the object. */
    @Override
    public InternalTimedQueue clone() {
        return new InternalTimedQueue(this);
    }
}
