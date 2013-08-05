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

import org.bitbucket.afsantos.jtransaction.common.AccessMode;

import org.bitbucket.afsantos.jtransaction.resources.Resource;
import org.bitbucket.afsantos.jtransaction.resources.ResourceState;

/**
 * ReadOnlyController
 * 
 * @author afs
 * @version 2013
*/

final class ReadOnlyController extends ResourceController {
    private static final String READ_ONLY = "read-only handle";

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ReadOnlyController. */
    ReadOnlyController(Resource r, String id) { super(r, id); }


    /** Parameter constructor of objects of class ReadOnlyController. */
    ReadOnlyController(Resource r, String id, ReadWriteListener rw) {
        super(r, id, rw);
    }


    /** Copy constructor of objects of class ReadOnlyController. */
    private ReadOnlyController(ReadOnlyController instance) {
        super(instance);
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** Throws a ReadOnlyHandleException.
     * This handle does not support write operations.
     */
    @Override
    public void write(ResourceState state) throws ReadOnlyHandleException {
        throw new ReadOnlyHandleException(READ_ONLY);
    }


    /** Does nothing. A reader has no changes to commit. */
    @Override
    protected void commit() {
        checkCanCommit();
        setStatus(Status.COMMITTED);
    }

    /** Does nothing. A reader has no changes to roll back. */
    @Override
    protected void rollback() {
        checkCanRollback();
        setStatus(Status.EXPIRED);
    }

    /** Does nothing. A reader has no commits to update. */
    @Override
    protected void update() {
        checkCanUpdate();
        setStatus(Status.EXPIRED);
    }


    /** */
    @Override
    protected void acquireResource() {
        acquireResource(AccessMode.READ);
    }



    /**************************************************************************
     * Clone
    **************************************************************************/

    /** Creates and returns a copy of this object. */
    @Override
    public ReadOnlyController clone() { return new ReadOnlyController(this); }
}
