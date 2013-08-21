package com.github.undo4j.resources;

import com.github.undo4j.common.Acquirable;
import com.github.undo4j.common.VersionedObject;

/**
 * Resource
 * 
 * @author afs
 * @version 2013
 */

public interface Resource<T> extends Acquirable, VersionedObject<ResourceState<T>> {
	/**
	 * Checks whether this resource can be accessed, in its current state.
	 */
	boolean isAccessible();

	/**
	 * Sets whether this resource can be accessed.
	 */
	void setAccessible(boolean accessible);

	/**
	 * Checks whether this resource is in a consistent state. The level of
	 * consistency guaranteed is, at least, that commit or rollback
	 * executedOperations completed successfully.
	 */
	boolean isConsistent();

	/**
	 * Sets whether this resource is in a consistent state.
	 */
	void setConsistent(boolean isConsistent);

	/** Return this resource's unique identifier. */
	ResourceId getId();
}
