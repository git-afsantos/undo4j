package com.github.undo4j.common;

import java.util.concurrent.locks.Lock;

/**
 * LockStrategy
 * 
 * @author afs
 * @version 2013
 */

abstract class LockStrategy {
	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class LockStrategy. */
	protected LockStrategy() {
	}

	/**************************************************************************
	 * Public Methods
	 **************************************************************************/

	/** */
	protected abstract boolean acquire(Lock lock) throws InterruptedException;
}
