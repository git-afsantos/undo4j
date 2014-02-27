package com.github.undo4j;


/**
 * ResourceManager
 * 
 * @author afs
 * @version 2013
 */

interface ResourceManager {
    void acquire(TransactionId tid, ResourceId id,
        AccessMode mode, WaitStrategy strategy);

    void release(TransactionId tid, ResourceId id);
}
