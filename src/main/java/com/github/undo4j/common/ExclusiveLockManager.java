package com.github.undo4j.common;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ExclusiveLockManager
 * 
 * @author afs
 * @version 2013
 */

final class ExclusiveLockManager extends StrategizedLockManager {
	// instance variables
	private final ReentrantLock lock;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Parameter constructor of objects of class ExclusiveLockManager. */
	ExclusiveLockManager(LockStrategy lockStrategy) {
		super(IsolationLevel.EXCLUSIVE, lockStrategy);
		lock = new ReentrantLock();
	}

	/** Copy constructor of objects of class ExclusiveLockManager. */
	ExclusiveLockManager(ExclusiveLockManager instance) {
		super(instance);
		lock = new ReentrantLock();
	}

	/**************************************************************************
	 * Public Methods
	 **************************************************************************/

	/** */
	@Override
	public boolean acquire(AccessMode mode) throws InterruptedException {
		return getLockStrategy().acquire(lock);
	}

	/** */
	@Override
	public void release() {
		lock.unlock();
	}

	/** */
	@Override
	public ExclusiveLockManager clone() {
		return new ExclusiveLockManager(this);
	}
}
