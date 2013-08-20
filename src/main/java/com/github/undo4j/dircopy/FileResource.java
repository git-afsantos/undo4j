package com.github.undo4j.dircopy;

import com.github.undo4j.common.LockManagers;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.StatelessResource;

/**
 * FileResource
 * 
 * @author afs
 * @version 2013
*/

public final class FileResource extends StatelessResource<String> {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class FileResource. */
    public FileResource(String path) {
        super(new InternalFile(path), LockManagers.newNullLockManager());
    }

    /** Parameter constructor of objects of class FileResource. */
    public FileResource(String path, boolean allowExistence) {
        super(new InternalFile(path, allowExistence), LockManagers.newNullLockManager());
    }


    /** Copy constructor of objects of class FileResource. */
    private FileResource(FileResource instance) {
        super(instance);
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    // ...



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public void commit() {}

    /** */
    @Override
    public void update() {}

    /** */
    @Override
    public void rollback() {
        InternalResource<String> r = getInternalResource();
        if (!(r instanceof InternalFile)) { return; }
        ((InternalFile) r).delete();
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    // ...



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** */
    @Override
    public String toString() {
        return getInternalResource().toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public FileResource clone() { return new FileResource(this); }
}
