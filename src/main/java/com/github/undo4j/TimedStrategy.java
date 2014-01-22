package com.github.undo4j;

import com.github.undo4j.AcquireStrategy;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


/**
 * TimedStrategy
 * 
 * @author afs
 * @version 2013
 */

final class TimedStrategy extends LockStrategy {

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
    protected AcquireStrategy getAcquireStrategy() {
        return AcquireStrategy.TIMED;
    }


    /**
     * Tries to acquire the lock within the time period specified on
     * construction. Returns true if the lock has been acquired. Returns false
     * otherwise.
     */
    @Override
    protected boolean acquire(final Lock lock) throws InterruptedException {
        return lock.tryLock(time, unit);
    }
}
