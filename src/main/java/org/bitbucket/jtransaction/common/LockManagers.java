package org.bitbucket.jtransaction.common;

import java.util.concurrent.TimeUnit;

/**
 * LockManagers
 * 
 * @author afs
 * @version 2013
*/

public final class LockManagers {
    private static final String ISOLATION = "Unsupported isolation level";
    private static final String STRATEGY = "Unsupported acquire strategy";
    
    private static final long DEFAULT_TIME = 0L;
    private static final TimeUnit DEFAULT_UNIT = TimeUnit.MILLISECONDS;
    
    private static final LockStrategy INSTANT = new InstantStrategy();
    private static final LockStrategy BLOCKING = new BlockingStrategy();
    private static final LockStrategy INTERRUPT = new InterruptibleStrategy();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class LockManagers. */
    private LockManagers() {
        throw new UnsupportedOperationException();
    }


    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    public static LockManager newNullLockManager() {
    	return new NullLockManager();
    }


    /** */
    public static LockManager newLockManager(
        IsolationLevel isolation, AcquireStrategy strategy
    ) {
        Check.checkArgument(isolation);
        Check.checkArgument(strategy);
        if (isolation == IsolationLevel.NONE) { return new NullLockManager(); }
        LockStrategy lockStrategy = newLockStrategy(strategy);
        return newIsolatedLockManager(isolation, lockStrategy);
    }


    /** */
    public static LockManager newTimedLockManager(
        IsolationLevel isolation, long time, TimeUnit timeUnit
    ) {
        Check.checkArgument(isolation);
        Check.checkArgument(time >= 0);
        Check.checkArgument(timeUnit);
        if (isolation == IsolationLevel.NONE) { return new NullLockManager(); }
        LockStrategy lockStrategy = new TimedStrategy(time, timeUnit);
        return newIsolatedLockManager(isolation, lockStrategy);
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    private static LockStrategy newLockStrategy(AcquireStrategy strategy) {
        switch (strategy) {
            case INSTANT: return INSTANT;
            
            case TIMED: return new TimedStrategy(DEFAULT_TIME, DEFAULT_UNIT);
            
            case BLOCKING: return BLOCKING;
            
            case INTERRUPTIBLE: return INTERRUPT;
            
            default: throw new IllegalArgumentException(STRATEGY);
        }
    }


    /** */
    private static LockManager newIsolatedLockManager(
        IsolationLevel isolation, LockStrategy strategy
    ) {
        switch (isolation) {
            case EXCLUSIVE: return new ExclusiveLockManager(strategy);
            
            case READ_WRITE: return new ReadWriteLockManager(strategy);
            
            case READ_COMMITTED: return new ShareableLockManager(strategy);
            
            default: throw new IllegalArgumentException(ISOLATION);
        }
    }
}
