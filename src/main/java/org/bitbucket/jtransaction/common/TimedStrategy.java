package org.bitbucket.jtransaction.common;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * TimedStrategy
 * 
 * @author afs
 * @version 2013
*/

final class TimedStrategy extends LockStrategy {
    // instance variables
    private final long time;
    private final TimeUnit unit;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /**
     * Parameter constructor of objects of class TimedStrategy.
     */
    TimedStrategy(long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }


    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /**
     * Tries to acquire the lock within the time period specified
     * on construction.
     * Returns true if the lock has been acquired. Returns false otherwise.
     */
    @Override
    protected boolean acquire(Lock lock) throws InterruptedException {
        return lock.tryLock(time, unit);
    }



    /** */
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof TimedStrategy)) { return false; }
        TimedStrategy n = (TimedStrategy) o;
        return (time == n.time && unit == n.unit);
    }

    /** */
    @Override
    public int hashCode() {
        return 37 * ((int) (time ^ (time >>> 32))) + unit.hashCode();
    }
}
