package org.bitbucket.jtransaction.transactions;


/**
 * ResourceHandleProvider
 * 
 * @author afs
 * @version 2013
*/

public interface ResourceHandleProvider {
    /** */
    ResourceHandle getHandleFor(String id);
}
