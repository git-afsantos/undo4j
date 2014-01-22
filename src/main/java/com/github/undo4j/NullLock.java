package com.github.undo4j;


/**
 * NullLock
 * 
 * @author afs
 * @version 2013
 */

final class NullLock extends Lock {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class NullLock.
     */
    NullLock() { super(); }



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
            final AccessMode mode, final LockStrategy strat) {
        return super.isValid;
    }


    /** */
    @Override
    protected void release() {}
}
