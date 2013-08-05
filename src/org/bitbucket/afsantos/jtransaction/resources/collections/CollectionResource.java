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


package org.bitbucket.afsantos.jtransaction.resources.collections;

import org.bitbucket.afsantos.jtransaction.resources.InternalResource;
import org.bitbucket.afsantos.jtransaction.resources.StatelessResource;

/**
 * CollectionResource
 * 
 * @author afs
 * @version 2013
*/

final class CollectionResource extends StatelessResource {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class CollectionResource. */
    CollectionResource(InternalCollection collection) {
        super(collection);
    }


    /** Copy constructor of objects of class CollectionResource. */
    private CollectionResource(CollectionResource instance) {
        super(instance.getInternalCollection());
        setConsistent(isConsistent());
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    InternalCollection getInternalCollection() {
        InternalResource r = getInternalResource();
        if (r instanceof InternalCollection) {
            return ((InternalCollection) r).clone();
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
    public CollectionResource clone() { return new CollectionResource(this); }
}
