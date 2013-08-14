package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.AccessMode;

import org.bitbucket.jtransaction.resources.Resource;
import org.bitbucket.jtransaction.resources.ResourceState;

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
