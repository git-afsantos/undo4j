package com.github.undo4j;


/**
 * LockProvider
 * 
 * @author afs
 * @version 2013
 */

interface LockProvider {
    /**
     * @throws ResourceUnregisteredException
     */
    Lock getLock(ResourceId resource);
}
