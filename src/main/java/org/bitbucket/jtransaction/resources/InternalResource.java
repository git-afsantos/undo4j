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


package org.bitbucket.jtransaction.resources;


/**
 * InternalResource is the object to be passed to Resources.
 * These manage the access to InternalResources.
 * 
 * @author afs
 * @version 2013
*/

public interface InternalResource {
    /** Performs any necessary resource initialization.
     * This method is allowed to throw any exception.
    */
    void initialize() throws Exception;


    /** Performs any necessary resource clean up.
     * This method is allowed to throw any exception.
    */
    void dispose() throws Exception;


    /** Tests whether the given state is valid.
     */
    boolean isValidState(ResourceState state);


    /** Builds and returns a representation of this resource's state.
     * This method is allowed to throw any exception,
     * if a state can't be built.
    */
    ResourceState buildState() throws Exception;


    /** Applies the given state to this resource.
     * This method is allowed to throw any exception,
     * if the changes can't be applied.
    */
    void applyState(ResourceState state) throws Exception;
}
