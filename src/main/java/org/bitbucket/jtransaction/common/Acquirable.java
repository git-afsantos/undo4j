/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Andre Santos, Victor Miraldo
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.bitbucket.jtransaction.common;

/**
 * Acquirable
 * 
 * @author afs
 * @version 2013
*/

public interface Acquirable {
    /** Returns the implemented Isolation level on this object.
     */
    IsolationLevel getIsolationLevel();

    /** Tries to instantly acquire this object, for the given access mode.
     * This method can be used to implement the desired locking scheme.
     * Returns true if the object has been acquired.
     * Returns false otherwise.
     */
    boolean tryAcquireFor(AccessMode mode);

    /** Tries to acquire this object within the given time period,
     * in milliseconds, for the given access mode.
     * This method can be used to implement the desired locking scheme.
     * Returns true if the object has been acquired.
     * Returns false otherwise.
     */
    boolean tryAcquireFor(AccessMode mode, long millis);

    /** Acquires this object, blocking if necessary, for the given access mode.
     * This method can be used to implement the desired locking scheme.
     */
    void acquireFor(AccessMode mode);

    /** Releases this object, granted it has been acquired.
     * This method can be used to implement the desired locking scheme.
     */
    void release();
}
