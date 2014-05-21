package com.github.undo4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;


/**
 * TimedStrategy
 * 
 * @author afs
 * @version 2013
 */

final class TimedStrategy extends WaitStrategy {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    private final long time;
    private final TimeUnit unit;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class TimedStrategy.
     */
    TimedStrategy(final long time, final TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    protected WaitMethod getWaitMethod() { return WaitMethod.TIMED; }


    /**
     * Waits on the given condition for the time period specified on
     * construction. Returns true if the thread has been signalled.
     * Returns false otherwise.
     */
    @Override
    protected boolean waitOn(final Condition condition)
            throws InterruptedException {
        return condition.await(time, unit);
    }
}
