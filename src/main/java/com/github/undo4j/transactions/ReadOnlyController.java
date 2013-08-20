
package com.github.undo4j.transactions;

import com.github.undo4j.common.AccessMode;
import com.github.undo4j.resources.Resource;
import com.github.undo4j.resources.ResourceState;

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
    ReadOnlyController() { super(); }


    /** Copy constructor of objects of class ReadOnlyController. */
    private ReadOnlyController(ReadOnlyController instance) {
        super(instance);
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** Throws a ReadOnlyHandleException.
     * This handle does not support write executedOperations.
     */
    @Override
    protected <T> void write(Resource<T> r, ResourceState<T> s)
    		throws ReadOnlyControllerException {
        throw new ReadOnlyControllerException(READ_ONLY);
    }


    /** Does nothing. A reader has no changes to commit. */
    @Override
    protected <T> void commit(Resource<T> resource) {
        checkCanCommit();
        setStatus(Status.COMMITTED);
    }

    /** Does nothing. A reader has no changes to roll back. */
    @Override
    protected <T> void rollback(Resource<T> resource) {
        checkCanRollback();
        setStatus(Status.EXPIRED);
    }

    /** Does nothing. A reader has no commits to update. */
    @Override
    protected <T> void update(Resource<T> resource) {
        checkCanUpdate();
        setStatus(Status.EXPIRED);
    }


    /** */
    @Override
    protected <T> void acquireResource(Resource<T> resource) {
        acquireResource(resource, AccessMode.READ);
    }



    /**************************************************************************
     * Clone
    **************************************************************************/

    /** Creates and returns a copy of this object. */
    @Override
    public ReadOnlyController clone() { return new ReadOnlyController(this); }
}
