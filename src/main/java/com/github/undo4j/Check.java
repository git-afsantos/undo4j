package com.github.undo4j;


/**
 * Check
 * 
 * @author afs
 * @version 2013
 */

public final class Check {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final String NULL    = "Object is null";
    private static final String FALSE   = "Condition is false";



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class Check.
     */
    private Check() {
        throw new AssertionError();
    }



    /*************************************************************************\
     *  Predicates
    \*************************************************************************/

    /** */
    public static boolean isNull(final Object o) { return o == null; }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static void checkNotNull(final Object o) {
        checkNotNull(NULL, o);
    }

    /** */
    public static void checkNotNull(final String msg, final Object o) {
        if (o == null) { throw new NullPointerException(msg); }
    }

    /** */
    public static <T> void checkNoNulls(final T[] objs) {
        checkNoNulls(NULL, objs);
    }

    /** */
    public static <T> void checkNoNulls(final String msg, final T[] objs) {
        if (objs == null) { throw new NullPointerException(msg); }
        for (Object o: objs) {
            if (o == null) { throw new NullPointerException(msg); }
        }
    }

    /** */
    public static void checkNoNulls(final Iterable<? extends Object> objs) {
        checkNoNulls(NULL, objs);
    }

    /** */
    public static void checkNoNulls(final String msg,
            final Iterable<? extends Object> objs) {
        if (objs == null) { throw new NullPointerException(msg); }
        for (Object o: objs) {
            if (o == null) { throw new NullPointerException(msg); }
        }
    }

    /** */
    public static void checkArgument(final Object o) {
        checkArgument(NULL, o != null);
    }

    /** */
    public static void checkArgument(final boolean predicate) {
        checkArgument(FALSE, predicate);
    }

    /** */
    public static void checkArgument(final String msg, final Object o) {
        checkArgument(msg, o != null);
    }

    /** */
    public static void checkArgument(
            final String msg,
            final boolean predicate) {
        if (!predicate) { throw new IllegalArgumentException(msg); }
    }
}
