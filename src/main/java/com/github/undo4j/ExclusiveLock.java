package com.github.undo4j;

import java.util.concurrent.locks.Condition;


/**
 * WARNING: not reentrant
 * 
 * @author afs
 * @version 2014-01-29
 */

final class ExclusiveLock extends ResourceLock {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** */
    private final Condition condition;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of class ExclusiveLock.
     */
    ExclusiveLock(final ResourceId id, final Condition condition) {
        super(id);
        assert condition != null;
        this.condition = condition;
    }

    /**
     *  Parameter constructor of class ExclusiveLock.
     */
    ExclusiveLock(final ResourceId id, final AcquireListener listener,
            final Condition condition) {
        super(id, listener);
        assert condition != null;
        this.condition = condition;
    }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    protected IsolationLevel getIsolationLevel() {
        return IsolationLevel.EXCLUSIVE;
    }


    /** */
    @Override
    protected boolean acquire(
            final TransactionId tid,
            final AccessMode mode,
            final WaitStrategy strat) throws InterruptedException {
        assert tid != null && mode != null && strat != null;
        // Fail if lock is invalid.
        if (!super.isValid()) { return false; }
        // Wait if there's another owner.
        boolean success = true;
        while (success && super.hasOwner()) {
            super.notifyWaiting(tid);
            try     { success = strat.waitOn(condition); }
            finally { super.notifyNotWaiting(tid); }
        }
        // Register ownership, if acquired.
        if (success) { super.notifyAcquired(tid); }
        return success;
    }


    /** */
    @Override
    protected void release(final TransactionId tid) {
        assert tid != null && super.hasOwner();
        // Clear ownership.
        super.notifyReleased(tid);
        // Wake up next contender.
        condition.signal();
    }
}
