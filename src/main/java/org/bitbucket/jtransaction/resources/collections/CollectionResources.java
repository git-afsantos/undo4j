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
    public static <T> Resource<T> newInstantQueue(T type) {
        return new CollectionResource<T>(new InternalInstantQueue<T>());
    }

    /** */
    public static <T> Resource<T> newInstantQueue(T type, int cap) {
        return new CollectionResource<T>(new InternalInstantQueue<T>(cap));
    }

    /** */
    public static <T> Resource<T> newInstantQueue(
        int capacity, Validator<ResourceState<T>> validator
    ) {
        return new CollectionResource<T>(new InternalInstantQueue<T>(
            capacity, validator
        ));
    }


    /**************************************************************************
     * Blocking Queue
    **************************************************************************/

    /** */
    public static <T> Resource<T> newBlockingQueue(T type) {
        return new CollectionResource<T>(new InternalBlockingQueue<T>());
    }

    /** */
    public static <T> Resource<T> newBlockingQueue(T type, int cap) {
        return new CollectionResource<T>(new InternalBlockingQueue<T>(cap));
    }

    /** */
    public static <T> Resource<T> newBlockingQueue(
        int capacity, Validator<ResourceState<T>> validator
    ) {
        return new CollectionResource<T>(new InternalBlockingQueue<T>(
            capacity, validator
        ));
    }



    /**************************************************************************
     * Timed Queue
    **************************************************************************/

    /** */
    public static <T> Resource<T> newTimedQueue(T type) {
        return new CollectionResource<T>(new InternalTimedQueue<T>());
    }

    /** */
    public static <T> Resource<T> newTimedQueue(
        T type, int cap, long wait
    ) {
        return new CollectionResource<T>(new InternalTimedQueue<T>(cap, wait));
    }

    /** */
    public static <T> Resource<T> newTimedQueue(
        int capacity, long wait, Validator<ResourceState<T>> validator
    ) {
        return new CollectionResource<T>(new InternalTimedQueue<T>(
            capacity, wait, validator
        ));
    }
}
