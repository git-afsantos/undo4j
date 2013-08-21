package com.github.undo4j.resources;

/**
 * NormalState
 * 
 * @author afs
 * @version 2013
 */

public final class NormalState<T> extends AbstractState<T> {
	// instance variables
	private T value;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class NormalState. */
	public NormalState() {
		value = null;
	}

	/** Parameter constructor of objects of class NormalState. */
	public NormalState(T val) {
		value = val;
	}

	/** Copy constructor of objects of class NormalState. */
	private NormalState(NormalState<? extends T> instance) {
		value = instance.get();
	}

	/**************************************************************************
	 * Getters
	 **************************************************************************/

	/** */
	@Override
	public T get() {
		return value;
	}

	/**************************************************************************
	 * Setters
	 **************************************************************************/

	/** */
	@Override
	public void set(T val) {
		value = val;
	}

	/**************************************************************************
	 * Predicates
	 **************************************************************************/

	/** */
	@Override
	public boolean isNull() {
		return value == null;
	}

	/**************************************************************************
	 * Equals, HashCode, ToString & Clone
	 **************************************************************************/

	/** Creates and returns a (deep) copy of this object. */
	@Override
	public NormalState<T> clone() {
		return new NormalState<T>(this);
	}
}
