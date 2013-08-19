package com.github.undo4j.resources.collections;

import com.github.undo4j.common.Validator;
import com.github.undo4j.resources.ResourceState;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * InternalBlockingQueue
 * 
 * @author afs
 * @version 2013
*/

final class InternalBlockingQueue<T> extends InternalConcurrentQueue<T> {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class InternalBlockingQueue. */
    InternalBlockingQueue() { this(Integer.MAX_VALUE); }


    /** Parameter constructor of objects of class InternalBlockingQueue. */
    InternalBlockingQueue(int cap) {
        super(cap, new LinkedBlockingQueue<ResourceState<T>>(cap));
    }

    /** Parameter constructor of objects of class InternalBlockingQueue. */
    InternalBlockingQueue(int cap, Validator<ResourceState<T>> val) {
        super(cap, new LinkedBlockingQueue<ResourceState<T>>(cap), val);
    }


    /** Copy constructor of objects of class InternalBlockingQueue. */
    InternalBlockingQueue(InternalBlockingQueue<T> instance) {
        super(instance);
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

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
        return getQueueReference().take();
    }

    /** */
    @Override
    public void applyState(ResourceState<T> state) throws InterruptedException {
        checkValidState(state);
        getQueueReference().put(state);
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
    public InternalBlockingQueue<T> clone() {
        return new InternalBlockingQueue<T>(this);
    }
}
