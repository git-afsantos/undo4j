package org.bitbucket.jtransaction.resources.collections;

import org.bitbucket.jtransaction.common.LockManager;

import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.StatelessResource;

/**
 * CollectionResource
 * 
 * @author afs
 * @version 2013
*/

final class CollectionResource<T> extends StatelessResource<T> {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class CollectionResource. */
    CollectionResource(
		InternalCollection<T> collection, LockManager lockManager
	) {
        super(collection, lockManager);
    }


    /** Copy constructor of objects of class CollectionResource. */
    private CollectionResource(CollectionResource<T> instance) {
        super(instance.getInternalCollection(), instance.getLockManager());
        setConsistent(isConsistent());
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    InternalCollection<T> getInternalCollection() {
        InternalResource<T> r = getInternalResource();
        if (r instanceof InternalCollection) {
            return ((InternalCollection<T>) r).clone();
        } else { return null; }
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    // ...



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public CollectionResource<T> clone() {
        return new CollectionResource<T>(this);
    }
}
