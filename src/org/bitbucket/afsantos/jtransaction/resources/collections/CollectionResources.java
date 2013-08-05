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
import org.bitbucket.afsantos.jtransaction.resources.Resource;
import org.bitbucket.afsantos.jtransaction.resources.ResourceState;

/**
 * CollectionResources
 * 
 * @author afs
 * @version 2013
*/

public final class CollectionResources {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class CollectionResources. */
    private CollectionResources() {
        throw new UnsupportedOperationException();
    }



    /**************************************************************************
     * Instant Queue
    **************************************************************************/

    /** */
    public static Resource newInstantQueue() {
        return new CollectionResource(new InternalInstantQueue());
    }

    /** */
    public static Resource newInstantQueue(int capacity) {
        return new CollectionResource(new InternalInstantQueue(capacity));
    }

    /** */
    public static Resource newInstantQueue(
        int capacity, Validator<ResourceState> validator
    ) {
        return new CollectionResource(new InternalInstantQueue(
            capacity, validator
        ));
    }


    /**************************************************************************
     * Blocking Queue
    **************************************************************************/

    /** */
    public static Resource newBlockingQueue() {
        return new CollectionResource(new InternalBlockingQueue());
    }

    /** */
    public static Resource newBlockingQueue(int capacity) {
        return new CollectionResource(new InternalBlockingQueue(capacity));
    }

    /** */
    public static Resource newBlockingQueue(
        int capacity, Validator<ResourceState> validator
    ) {
        return new CollectionResource(new InternalBlockingQueue(
            capacity, validator
        ));
    }



    /**************************************************************************
     * Timed Queue
    **************************************************************************/

    /** */
    public static Resource newTimedQueue() {
        return new CollectionResource(new InternalTimedQueue());
    }

    /** */
    public static Resource newTimedQueue(int capacity, long wait) {
        return new CollectionResource(new InternalTimedQueue(capacity, wait));
    }

    /** */
    public static Resource newTimedQueue(
        int capacity, long wait, Validator<ResourceState> validator
    ) {
        return new CollectionResource(new InternalTimedQueue(
            capacity, wait, validator
        ));
    }
}
