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


package org.bitbucket.afsantos.jtransaction.resources;


/**
 * StatelessResource
 * 
 * @author afs
 * @version 2013
*/

public abstract class StatelessResource extends AbstractResource {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class StatelessResource. */
    public StatelessResource(InternalResource resource) {
        super(resource);
    }


    /** Copy constructor of objects of class StatelessResource. */
    protected StatelessResource(StatelessResource instance) {
        super(instance);
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public final ResourceState read() {
        try { return getInternalResource().buildState(); }
        catch (Exception e) {
            throw new ResourceReadException(e.getMessage(), e);
        }
    }

    /** */
    @Override
    public final void write(ResourceState state) {
        try { getInternalResource().applyState(state); }
        catch (Exception e) {
            throw new ResourceWriteException(e.getMessage(), e);
        }
    }
}
