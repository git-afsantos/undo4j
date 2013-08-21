package com.github.undo4j.common;

/**
 * Copyable
 * 
 * @author afs
 * @version 2013
 */

public interface Copyable<T extends Copyable<T>> extends Cloneable {
	T clone();
}
