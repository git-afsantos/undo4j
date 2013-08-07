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

import org.bitbucket.jtransaction.common.Acquirable;
import org.bitbucket.jtransaction.common.Disposable;
import org.bitbucket.jtransaction.common.Initializable;
import org.bitbucket.jtransaction.common.VersionedObject;


/**
 * Resource
 * 
 * @author afs
 * @version 2013
*/

public interface Resource extends Acquirable,
        VersionedObject<ResourceState>, Initializable, Disposable {
    /** NullState constant to be used by implementing classes. */
    ResourceState NULL_STATE = new NullState();


    /** Resource status enumeration. */
    enum Status {
        UPDATED, CHANGED, COMMITTED;

        /** Returns the initial status. */
        static Status initialStatus() { return UPDATED; }
    }


    /** Checks whether this resource can be accessed, in its current state.
     */
    boolean isAccessible();


    /** Sets whether this resource can be accessed.
     */
    void setAccessible(boolean accessible);


    /** Checks whether this resource is in a consistent state.
     * The level of consistency guaranteed is, at least, that commit or rollback
     * operations completed successfully.
     */
    boolean isConsistent();


    /** Sets whether this resource is in a consistent state.
     */
    void setConsistent(boolean isConsistent);
}
