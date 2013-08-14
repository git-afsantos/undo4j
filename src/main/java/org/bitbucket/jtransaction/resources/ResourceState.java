package org.bitbucket.jtransaction.resources;

import org.bitbucket.jtransaction.common.Copyable;

/**
 * ResourceState
 * 
 * @author afs
 * @version 2013
*/

public interface ResourceState<T> extends Copyable<ResourceState<T>> {
    /** Checks whether this state has any contents. */
    boolean isNull();

    /** Retrieve the current value of this state. */
    T get();

    /** Set the current value of this state. */
    void set(T value);
}
