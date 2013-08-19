package com.github.undo4j.resources.collections;

import com.github.undo4j.common.Validator;
import com.github.undo4j.resources.ResourceState;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * InternalTimedQueue
 * 
 * @author afs
 * @version 2013
*/

final class InternalTimedQueue<T> extends InternalConcurrentQueue<T> {
    private static final long DEFAULT_WAIT = 1000L;

    // instance variables
    private final long wait;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue() { this(Integer.MAX_VALUE, DEFAULT_WAIT); }


    /** Parameter constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue(int cap) { this(cap, DEFAULT_WAIT); }

    /** Parameter constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue(long maxWait) { this(Integer.MAX_VALUE, maxWait); }

    /** Parameter constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue(int cap, long maxWait) {
        super(cap, new LinkedBlockingQueue<ResourceState<T>>(cap));
        wait = maxWait;
    }

    /** Parameter constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue(int cap, long maxWait, Validator<ResourceState<T>> val) {
        super(cap, new LinkedBlockingQueue<ResourceState<T>>(cap), val);
        wait = maxWait;
    }


    /** Copy constructor of objects of class InternalTimedQueue. */
    InternalTimedQueue(InternalTimedQueue<T> instance) {
        super(instance);
        wait = instance.getWaitTime();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    long getWaitTime() { return wait; }

    /** */
    @Override
    protected BlockingQueue<ResourceState<T>> getQueue() {
        int cap = getCapacity();
        BlockingQueue<ResourceState<T>> queue = getQueueReference();
        BlockingQueue<ResourceState<T>> copy = new LinkedBlockingQueue<>(cap);
        for (ResourceState<T> s : queue) { copy.offer(s.clone()); }
        return copy;
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    // ...



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public ResourceState<T> buildState() throws InterruptedException {
        return getQueueReference().poll(wait, TimeUnit.MILLISECONDS);
    }

    /** */
    @Override
    public void applyState(ResourceState<T> state) throws InterruptedException {
        checkValidState(state);
        getQueueReference().offer(state, wait, TimeUnit.MILLISECONDS);
    }

    



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    // ...



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Returns a copy of the object. */
    @Override
    public InternalTimedQueue<T> clone() {
        return new InternalTimedQueue<T>(this);
    }
}
