package org.bitbucket.afsantos.jtransaction.resources.collections;

import org.bitbucket.afsantos.jtransaction.common.Validator;
import org.bitbucket.afsantos.jtransaction.resources.ResourceState;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * InternalInstantQueue
 * 
 * @author afs
 * @version 2013
*/

final class InternalInstantQueue<T> extends InternalConcurrentQueue<T> {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class InternalInstantQueue. */
    InternalInstantQueue() { this(Integer.MAX_VALUE); }


    /** Parameter constructor of objects of class InternalInstantQueue. */
    InternalInstantQueue(int cap) {
        super(cap, new LinkedBlockingQueue<ResourceState<T>>(cap));
    }

    /** Parameter constructor of objects of class InternalInstantQueue. */
    InternalInstantQueue(int cap, Validator<ResourceState<T>> val) {
        super(cap, new LinkedBlockingQueue<ResourceState<T>>(cap), val);
    }


    /** Copy constructor of objects of class InternalInstantQueue. */
    InternalInstantQueue(InternalInstantQueue<T> instance) {
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
    public ResourceState<T> buildState() {
        return getQueueReference().poll();
    }

    /** */
    @Override
    public void applyState(ResourceState<T> state) {
        checkValidState(state);
        getQueueReference().offer(state);
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
    public InternalInstantQueue<T> clone() {
        return new InternalInstantQueue<T>(this);
    }
}
