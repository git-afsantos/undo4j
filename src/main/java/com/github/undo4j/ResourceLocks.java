package com.github.undo4j;

import java.util.concurrent.locks.Lock;

/**
 * ResourceLocks
 * 
 * @author afs
 * @version 2013
 */

final class ResourceLocks {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final String UNSUPPORTED = "Unsupported isolation level";



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class ResourceLocks.
     */
    private ResourceLocks() { throw new AssertionError(); }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    static ResourceLock noLock(final ResourceId id) {
        return new NullLock(id);
    }

    /** */
    static ResourceLock noLock(final ResourceId id,
            final ResourceLock.AcquireListener listener) {
        return new NullLock(id, listener);
    }


    /** */
    static ResourceLock newLock(
            final IsolationLevel isolation,
            final ResourceId id,
            final Lock lock) {
        assert isolation != null && lock != null;
        switch (isolation) {
            case EXCLUSIVE:
                return new ExclusiveLock(id, lock.newCondition());

            case READ_WRITE:
                return new ReadWriteLock(id,
                    lock.newCondition(), lock.newCondition());

            case NONE:
                return new NullLock(id);

            default:
                throw new AssertionError(UNSUPPORTED);
        }
    }


    /** */
    static ResourceLock newLock(
            final IsolationLevel isolation,
            final ResourceId id,
            final ResourceLock.AcquireListener listener,
            final Lock lock) {
        assert isolation != null && lock != null;
        switch (isolation) {
            case EXCLUSIVE:
                return new ExclusiveLock(id, listener, lock.newCondition());

            case READ_WRITE:
                return new ReadWriteLock(id, listener,
                    lock.newCondition(), lock.newCondition());

            case NONE:
                return new NullLock(id, listener);

            default:
                throw new AssertionError(UNSUPPORTED);
        }
    }
}
