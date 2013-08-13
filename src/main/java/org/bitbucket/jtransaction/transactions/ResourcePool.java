package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.resources.Resource;

/**
 * ResourcePool
 * 
 * @author afs
 * @version 2013
*/

public interface ResourcePool {
    /** */
    void putResource(String id, Resource r);

    /** */
    void removeResource(String id);
}
