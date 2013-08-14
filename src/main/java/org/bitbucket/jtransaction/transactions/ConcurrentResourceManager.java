package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.resources.Resource;

/**
 * ConcurrentResourceManager
 * 
 * @author afs
 * @version 2013
*/

final class ConcurrentResourceManager extends ResourceManager {
    // instance variables
    private final Object monitor = new Object();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class ConcurrentResourceManager. */
    public ConcurrentResourceManager() { super(); }


    /** Parameter constructor of objects of class ConcurrentResourceManager. */
    public ConcurrentResourceManager(boolean log) { super(log); }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    @Override
    public Resource getResource(String key) {
        synchronized (this.monitor) { return super.getResource(key); }
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public void putResource(String id, Resource res) {
        synchronized (this.monitor) { super.putResource(id, res); }
    }

    /** */
    @Override
    public void removeResource(String id) {
        synchronized (this.monitor) { super.removeResource(id); }
    }
}
