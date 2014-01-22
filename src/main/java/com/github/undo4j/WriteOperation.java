package com.github.undo4j;


/**
 * By definition, writing changes the state.
 * This implies the need for an undo operation.
 * With an undo operation, it is possible to roll back.
 *
 * @author afs
 * @version 2013
 */

public interface WriteOperation {
    /** */
    void write();

    /** Called by the system, when rolling back. */
    void undo();

    /** */
    Iterable<ResourceId> resources();
}
