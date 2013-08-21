package com.github.undo4j.resources.collections;

import com.github.undo4j.common.Validator;
import com.github.undo4j.resources.ResourceState;

import java.util.concurrent.BlockingQueue;

/**
 * InternalConcurrentQueue
 * 
 * @author afs
 * @version 2013
 */

abstract class InternalConcurrentQueue<T> extends InternalCollection<T> {
	// instance variables
	private final int capacity;
	private final BlockingQueue<ResourceState<T>> queue;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Parameter constructor of objects of class InternalConcurrentQueue. */
	protected InternalConcurrentQueue(int capacity, BlockingQueue<ResourceState<T>> queue) {
		super();
		this.capacity = capacity;
		this.queue = queue;
	}

	/** Parameter constructor of objects of class InternalConcurrentQueue. */
	protected InternalConcurrentQueue(int capacity, BlockingQueue<ResourceState<T>> queue,
			Validator<ResourceState<T>> validator) {
		super(validator);
		this.capacity = capacity;
		this.queue = queue;
	}

	/** Copy constructor of objects of class InternalConcurrentQueue. */
	protected InternalConcurrentQueue(InternalConcurrentQueue<T> instance) {
		super(instance);
		this.capacity = instance.getCapacity();
		this.queue = instance.getQueue();
	}

	/**************************************************************************
	 * Getters
	 **************************************************************************/

	/** */
	protected final int getCapacity() {
		return capacity;
	}

	/** */
	protected abstract BlockingQueue<ResourceState<T>> getQueue();

	/** */
	protected final BlockingQueue<ResourceState<T>> getQueueReference() {
		return this.queue;
	}

	/**************************************************************************
	 * Predicates
	 **************************************************************************/

	// ...

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

	/**
	 * Equivalence relation. Contract (for any non-null reference values x, y,
	 * and z): Reflexive: x.equals(x). Symmetric: x.equals(y) iff y.equals(x).
	 * Transitive: if x.equals(y) and y.equals(z), then x.equals(z).
	 * Consistency: successive calls (with no modification of the equality
	 * fields) return the same result. x.equals(null) should return false.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof InternalConcurrentQueue))
			return false;
		InternalConcurrentQueue<?> n = (InternalConcurrentQueue<?>) o;
		return this.queue.equals(n.getQueue());
	}

	/**
	 * Contract: Consistency: successive calls (with no modification of the
	 * equality fields) return the same code. Function: two equal objects have
	 * the same (unique) hash code. (Optional) Injection: unequal objects have
	 * different hash codes. Common practices: boolean: calculate (f ? 0 : 1);
	 * byte, char, short or int: calculate (int) f; long: calculate (int) (f ^
	 * (f >>> 32)); float: calculate Float.floatToIntBits(f); double: calculate
	 * Double.doubleToLongBits(f) and handle the return value like every long
	 * value; Object: use (f == null ? 0 : f.hashCode()); Array: recursion and
	 * combine the values. Formula: hash = prime * hash + codeForField
	 */
	@Override
	public int hashCode() {
		return this.queue.hashCode();
	}

	/** Returns a string representation of the object. */
	@Override
	public String toString() {
		return this.queue.toString();
	}

	/** Creates and returns a (deep) copy of this object. */
	@Override
	public abstract InternalConcurrentQueue<T> clone();
}
