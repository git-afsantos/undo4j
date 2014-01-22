package com.github.undo4j;


/**
 * Locks
 * 
 * @author afs
 * @version 2013
 */

final class Locks {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final String UNSUPPORTED = "Unsupported isolation level";



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class Locks.
     */
    private Locks() { throw new AssertionError(); }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    static Lock noLock() {
        return new NullLock();
    }


    /** */
    static Lock newLock(final IsolationLevel isolation) {
        assert isolation != null;
        switch (isolation) {
            case EXCLUSIVE:     return new ExclusiveLock();
            case READ_WRITE:    return new ReadWriteLock();
            case NONE:          return new NullLock();
            default:            throw new AssertionError(UNSUPPORTED);
        }
    }
}
