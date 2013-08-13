package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.resources.ResourceState;

/**
 * ResourceHandle is a wrapper for a resource, to control access to it.
 * 
 * @author afs
 * @version 2013
*/

public interface ResourceHandle {
    /** Asks the resource to retrieve a state object, for reading.
     */
    ResourceState read();

    /** Asks the resource to write a state.
     */
    void write(ResourceState state);

    /*
     * void tryRead(); // Only reads if resource is accessible
     * void tryWrite(); // same
     */
}
