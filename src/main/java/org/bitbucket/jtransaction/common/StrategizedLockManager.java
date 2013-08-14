package org.bitbucket.jtransaction.common;

/**
 * ReadWriteLockManager
 * 
 * @author afs
 * @version 2013
*/

abstract class StrategizedLockManager extends LockManager {
	// instance variables
	private final LockStrategy strategy;

	/**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class ReadWriteLockManager. */
	protected StrategizedLockManager(
		IsolationLevel isolationLevel, LockStrategy lockStrategy
	) {
		super(isolationLevel);
		this.strategy = lockStrategy;
	}


	/** Copy constructor of objects of class ReadWriteLockManager. */
	protected StrategizedLockManager(StrategizedLockManager instance) {
		super(instance);
		this.strategy = instance.getLockStrategy();
	}



	/**************************************************************************
     * Getters
    **************************************************************************/

	/** */
	protected final LockStrategy getLockStrategy() {
		return this.strategy;
	}
}
