package com.github.undo4j.resources;

/**
 * StateUtil
 * 
 * @author afs
 * @version 2013
 */

final class StateUtil {
	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class StateUtil. */
	private StateUtil() {
	}

	/**************************************************************************
	 * Predicates
	 **************************************************************************/

	// ...

	/**************************************************************************
	 * Public Methods
	 **************************************************************************/

	/** Returns a NullState if null. Returns a clone otherwise. */
	public static <T> ResourceState<T> cloneSafely(ResourceState<T> s) {
		if (s == null) {
			return new NullState<T>();
		}
		return s.clone();
	}

	/** Returns a clone if not null. Returns null otherwise. */
	public static <T> ResourceState<T> tryClone(ResourceState<T> s) {
		if (s == null) {
			return null;
		}
		return s.clone();
	}

	/** Returns a NullState if null. Returns the object itself, otherwise. */
	public static <T> ResourceState<T> identity(ResourceState<T> s) {
		if (s == null) {
			return new NullState<T>();
		}
		return s;
	}
}
