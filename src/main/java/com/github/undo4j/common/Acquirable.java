package com.github.undo4j.common;

/**
 * Acquirable
 * 
 * @author afs
 * @version 2013
 */

public interface Acquirable extends Isolated {
	/**
	 * Acquires this object, for the given AccessMode. May throw
	 * InterruptedException while waiting. Returns true if acquired; returns
	 * false otherwise.
	 */
	boolean acquire(AccessMode mode) throws InterruptedException;

	/**
	 * Releases this object, granted it has been acquired. This method can be
	 * used to implement the desired locking scheme.
	 */
	void release();
}
