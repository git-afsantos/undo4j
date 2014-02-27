package com.github.undo4j;

import java.util.concurrent.locks.Condition;


/**
 * BlockingStrategy
 * 
 * @author afs
 * @version 2013
 */

final class BlockingStrategy extends WaitStrategy {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class BlockingStrategy.
     */
    BlockingStrategy() { super(); }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    protected WaitMethod getWaitMethod() { return WaitMethod.BLOCKING; }


    /**
     * Blocks waiting while necessary. Returns true when the thread has
     * been signalled.
     */
    @Override
    protected boolean waitOn(final Condition condition)
            throws InterruptedException {
        condition.await();
        return true;
    }
}
