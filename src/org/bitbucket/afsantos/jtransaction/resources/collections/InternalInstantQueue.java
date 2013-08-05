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

final class InternalInstantQueue extends InternalConcurrentQueue {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class InternalInstantQueue. */
    InternalInstantQueue() { this(Integer.MAX_VALUE); }


    /** Parameter constructor of objects of class InternalInstantQueue. */
    InternalInstantQueue(int cap) {
        super(cap, new LinkedBlockingQueue<ResourceState>(cap));
    }

    /** Parameter constructor of objects of class InternalInstantQueue. */
    InternalInstantQueue(int cap, Validator<ResourceState> val) {
        super(cap, new LinkedBlockingQueue<ResourceState>(cap), val);
    }


    /** Copy constructor of objects of class InternalInstantQueue. */
    InternalInstantQueue(InternalInstantQueue instance) {
        super(instance);
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    @Override
    protected BlockingQueue<ResourceState> getQueue() {
        int cap = getCapacity();
        BlockingQueue<ResourceState> queue = getQueueReference();
        BlockingQueue<ResourceState> copy = new LinkedBlockingQueue<>(cap);
        for (ResourceState s : queue) { copy.offer(s.clone()); }
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
    public ResourceState buildState() {
        return getQueueReference().poll();
    }

    /** */
    @Override
    public void applyState(ResourceState state) {
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
    public InternalInstantQueue clone() {
        return new InternalInstantQueue(this);
    }
}
