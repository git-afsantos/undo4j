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


package org.bitbucket.afsantos.jtransaction.transactions;

import org.bitbucket.afsantos.jtransaction.resources.Resource;

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
