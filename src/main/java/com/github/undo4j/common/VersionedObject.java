package com.github.undo4j.common;


/**
 * VersionedObject
 * 
 * @author afs
 * @version 2013
*/

public interface VersionedObject<T> {
    /** Reads from the last checkpoint (stable/public version). */
    T read();
    /** Writes on the object. The resulting changes need to be committed. */
    void write(T state);
    /** Validates any changes made. The commit needs to be updated. */
    void commit();
    /** Reverts any changes made, which are not yet a checkpoint. */
    void rollback();
    /** Promotes the last committed changes to a checkpoint. */
    void update();
}
