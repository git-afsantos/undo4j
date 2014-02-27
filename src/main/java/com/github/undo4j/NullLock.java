package com.github.undo4j;


/**
 * NullLock
 * 
 * @author afs
 * @version 2014-01-29
 */

final class NullLock extends ResourceLock {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of class NullLock.
     */
    NullLock(final ResourceId id) {
        super(id);
    }


    /**
     *  Parameter constructor of class NullLock.
     */
    NullLock(final ResourceId id, final AcquireListener listener) {
        super(id, listener);
    }



    /*************************************************************************\
     *  Protected Methods
    \*************************************************************************/

    /** */
    @Override
    protected IsolationLevel getIsolationLevel() {
        return IsolationLevel.NONE;
    }


    /** */
    @Override
    protected boolean acquire(
            final TransactionId tid,
            final AccessMode mode,
            final WaitStrategy strat) {
        assert tid != null && mode != null && strat != null;
        boolean valid = super.isValid();
        if (valid) { super.notifyAcquired(tid); }
        return valid;
    }


    /** */
    @Override
    protected void release(final TransactionId tid) {
        assert tid != null && super.hasOwner();
        super.notifyReleased(tid);
    }
}
