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


package org.bitbucket.afsantos.jtransaction.common;


/**
 * Check
 * 
 * @author afs
 * @version 2013
*/

public final class Check {
    private static final String EMPTY_MSG = "";

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class Check. */
    private Check() { throw new UnsupportedOperationException(); }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    public static boolean isNull(Object o) { return o == null; }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    public static void checkNotNull(Object o) {
        checkNotNull(EMPTY_MSG, o);
    }

    /** */
    public static void checkNotNull(String msg, Object o) {
        if (o == null) { throw new NullPointerException(msg); }
    }


    /** */
    public static void checkArgument(Object o) {
        checkArgument(EMPTY_MSG, o != null);
    }

    /** */
    public static void checkArgument(boolean predicate) {
        checkArgument(EMPTY_MSG, predicate);
    }

    /** */
    public static void checkArgument(String msg, Object o) {
        checkArgument(msg, o != null);
    }

    /** */
    public static void checkArgument(String msg, boolean predicate) {
        if (!predicate) { throw new IllegalArgumentException(msg); }
    }
}
