package com.github.undo4j;

import com.github.undo4j.AcquireStrategy;

import java.util.concurrent.TimeUnit;


/**
 * LockStrategies
 * 
 * @author afs
 * @version 2013
 */

final class LockStrategies {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final String UNSUPPORTED = "Unsupported acquire strategy";

    private static final long DEFAULT_TIME      = 0L;
    private static final TimeUnit DEFAULT_UNIT  = TimeUnit.MILLISECONDS;

    private static final LockStrategy INSTANT   = new InstantStrategy();
    private static final LockStrategy BLOCKING  = new BlockingStrategy();
    private static final LockStrategy INTERRUPT = new InterruptibleStrategy();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class LockStrategies.
     */
    private LockStrategies() { throw new AssertionError(); }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    static LockStrategy newStrategy(final AcquireStrategy strat) {
        assert strat != null;
        switch (strat) {
            case INSTANT:
            return INSTANT;

            case TIMED:
            return new TimedStrategy(DEFAULT_TIME, DEFAULT_UNIT);

            case BLOCKING:
            return BLOCKING;

            case INTERRUPTIBLE:
            return INTERRUPT;

            default:
            throw new AssertionError(UNSUPPORTED);
        }
    }


    /** */
    static LockStrategy newTimedStrategy(
            final long time, final TimeUnit unit) {
        assert time >= 0;
        assert unit != null;
        return new TimedStrategy(time, unit);
    }
}
