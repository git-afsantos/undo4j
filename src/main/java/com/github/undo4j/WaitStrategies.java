package com.github.undo4j;

import java.util.concurrent.TimeUnit;


/**
 * WaitStrategies
 * 
 * @author afs
 * @version 2013
 */

final class WaitStrategies {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final String UNSUPPORTED = "Unsupported wait method";

    private static final long DEFAULT_TIME      = 1L;
    private static final TimeUnit DEFAULT_UNIT  = TimeUnit.MILLISECONDS;

    private static final WaitStrategy INSTANT   = new InstantStrategy();
    private static final WaitStrategy BLOCKING  = new BlockingStrategy();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class WaitStrategies.
     */
    private WaitStrategies() { throw new AssertionError(); }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    static WaitStrategy newStrategy(final WaitMethod strat) {
        assert strat != null;
        switch (strat) {
            case INSTANT:
            return INSTANT;

            case TIMED:
            return new TimedStrategy(DEFAULT_TIME, DEFAULT_UNIT);

            case BLOCKING:
            return BLOCKING;

            default:
            throw new AssertionError(UNSUPPORTED);
        }
    }


    /** */
    static WaitStrategy newTimedStrategy
            (final long time, final TimeUnit unit) {
        assert time >= 0;
        assert unit != null;
        return new TimedStrategy(time, unit);
    }
}
