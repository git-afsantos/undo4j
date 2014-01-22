package com.github.undo4j;


/**
 * By definition, reading does not change the state.
 * This implies the absence of an undo operation.
 * 
 * @author afs
 * @version 2013
 */

public interface ReadOperation<T> {
    /** */
    T read();

    /** */
    Iterable<ResourceId> resources();
}
