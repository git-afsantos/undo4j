package com.github.undo4j;


/**
 * Each transaction will be allowed to define its own custom
 * commit operation.
 * If no operation is defined, committing does nothing.
 * These operations should not modify the resource.
 * 
 * @author afs
 * @version 2013
 */

public interface CommitOperation {
    /** */
    void commit();

    /** Called by the system, when rolling back. */
    void undo();
}
