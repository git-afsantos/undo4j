package com.github.undo4j.resources.collections;

import com.github.undo4j.common.LockManagers;
import com.github.undo4j.common.Validator;
import com.github.undo4j.resources.Resource;
import com.github.undo4j.resources.ResourceState;

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
        return new CollectionResource<T>(
    		new InternalInstantQueue<T>(),
    		LockManagers.newNullLockManager()
		);
    }

    /** */
    public static <T> Resource<T> newInstantQueue(T type, int cap) {
        return new CollectionResource<T>(
    		new InternalInstantQueue<T>(cap),
    		LockManagers.newNullLockManager()
		);
    }

    /** */
    public static <T> Resource<T> newInstantQueue(
        int capacity, Validator<ResourceState<T>> validator
    ) {
        return new CollectionResource<T>(
    		new InternalInstantQueue<T>(capacity, validator),
    		LockManagers.newNullLockManager()
        );
    }


    /**************************************************************************
     * Blocking Queue
    **************************************************************************/

    /** */
    public static <T> Resource<T> newBlockingQueue(T type) {
        return new CollectionResource<T>(
    		new InternalBlockingQueue<T>(),
    		LockManagers.newNullLockManager()
		);
    }

    /** */
    public static <T> Resource<T> newBlockingQueue(T type, int cap) {
        return new CollectionResource<T>(
    		new InternalBlockingQueue<T>(cap),
    		LockManagers.newNullLockManager()
		);
    }

    /** */
    public static <T> Resource<T> newBlockingQueue(
        int capacity, Validator<ResourceState<T>> validator
    ) {
        return new CollectionResource<T>(
    		new InternalBlockingQueue<T>(capacity, validator),
    		LockManagers.newNullLockManager()
		);
    }



    /**************************************************************************
     * Timed Queue
    **************************************************************************/

    /** */
    public static <T> Resource<T> newTimedQueue(T type) {
        return new CollectionResource<T>(
    		new InternalTimedQueue<T>(),
    		LockManagers.newNullLockManager()
		);
    }

    /** */
    public static <T> Resource<T> newTimedQueue(
        T type, int cap, long wait
    ) {
        return new CollectionResource<T>(
    		new InternalTimedQueue<T>(cap, wait),
    		LockManagers.newNullLockManager()
		);
    }

    /** */
    public static <T> Resource<T> newTimedQueue(
        int capacity, long wait, Validator<ResourceState<T>> validator
    ) {
        return new CollectionResource<T>(
    		new InternalTimedQueue<T>(capacity, wait, validator),
    		LockManagers.newNullLockManager()
		);
    }
}
