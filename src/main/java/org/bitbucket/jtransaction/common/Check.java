package org.bitbucket.jtransaction.common;


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
