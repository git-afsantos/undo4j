package org.bitbucket.jtransaction.resources.collections;

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
    CollectionResource(InternalCollection<T> collection) {
        super(collection);
    }


    /** Copy constructor of objects of class CollectionResource. */
    private CollectionResource(CollectionResource<T> instance) {
        super(instance.getInternalCollection());
        setConsistent(isConsistent());
    }



    /**************************************************************************
     * Predicates
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
    public void rollback() {}



    /**************************************************************************
     * Private Methods
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
