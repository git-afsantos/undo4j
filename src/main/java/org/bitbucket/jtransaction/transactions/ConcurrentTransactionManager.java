/*
The MIT License (MIT)

Copyright (c) 2013 Andre Santos, Victor Miraldo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/


package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.resources.Resource;

/**
 * ConcurrentTransactionManager
 * 
 * @author afs
 * @version 2013
*/

abstract class ConcurrentTransactionManager
        extends AbstractTransactionManager {
    // instance variables
    private final Object monitor = new Object();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ConcurrentTransactionManager. */
    protected ConcurrentTransactionManager(ResourceManager rm) {
        super(rm);
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    @Override
    protected final Resource getResource(String id) {
        synchronized (this.monitor) { return super.getResource(id); }
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public final void putResource(String id, Resource resource) {
        // Throw exception if a transaction tries to add a resource.
        checkNotTransaction();
        // Delegate to resource manager.
        synchronized (this.monitor) {
            getResourceManager().putResource(id, resource);
        }
    }

    /** */
    @Override
    public final void removeResource(String id) {
        // Throw exception if a transaction tries to remove a resource.
        checkNotTransaction();
        // Delegate to resource manager.
        synchronized (this.monitor) {
            getResourceManager().removeResource(id);
        }
    }
}
