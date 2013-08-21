package com.github.undo4j.resources;

import static com.github.undo4j.common.Check.checkArgument;

/**
 * ImmutableNonNullState
 * 
 * @author vmiraldo
 * @version 2013
 */

public final class ImmutableNonNullState<T> extends AbstractState<T> {
	// instance variables
	private final T value;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Parameter constructor of objects of class ImmutableNonNullState. */
	public ImmutableNonNullState(T val) {
		checkArgument(val);
		value = val;
	}

	/** Copy constructor of objects of class ImmutableNonNullState. */
	private ImmutableNonNullState(ImmutableNonNullState<? extends T> inns) {
		value = inns.get();
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
		throw new UnsupportedOperationException("immutable state");
	}

	/**************************************************************************
	 * Predicates
	 **************************************************************************/

	/** */
	@Override
	public boolean isNull() {
		return false;
	}

	/**************************************************************************
	 * Public Methods
	 **************************************************************************/

	// ...

	/**************************************************************************
	 * Private Methods
	 **************************************************************************/

	// ...

	/**************************************************************************
	 * Equals, HashCode, ToString & Clone
	 **************************************************************************/

	/** Creates and returns a (deep) copy of this object. */
	@Override
	public ImmutableNonNullState<T> clone() {
		return new ImmutableNonNullState<T>(this);
	}
}
