package com.github.undo4j.transactions;

import com.github.undo4j.common.AccessMode;
import com.github.undo4j.common.IsolationLevel;

import static com.github.undo4j.common.Check.checkArgument;

/**
 * AbstractTransaction README: There are some commented lines. I don't have yet
 * a way to keep the statistics updated.
 * 
 * @author afs
 * @version 2013
 */

abstract class AbstractTransaction<T> implements Transaction<T> {
	// instance variables
	private Thread thread = Thread.currentThread();
	private final TransactionId id = TransactionId.newTransactionId();
	private final AccessMode mode;
	private final IsolationLevel isolation;
	private final TransactionListener listener;

	// private final TransactionStatistics stats = new TransactionStatistics();

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Parameter constructor of objects of class AbstractTransaction. */
	protected AbstractTransaction(AccessMode accessMode, IsolationLevel isolationLevel,
			TransactionListener transactionListener) {
		checkArgument("null access mode", accessMode);
		checkArgument("null isolation level", isolationLevel);
		checkArgument("null listener", transactionListener);
		this.mode = accessMode;
		this.isolation = isolationLevel;
		this.listener = transactionListener;
	}

	/** Copy constructor of objects of class AbstractTransaction. */
	protected AbstractTransaction(AbstractTransaction<T> instance) {
		this(instance.getAccessMode(), instance.getIsolationLevel(), instance.getListener());
	}

	/**************************************************************************
	 * Getters
	 **************************************************************************/

	/** */
	@Override
	public final TransactionId getId() {
		return this.id;
	}

	/** */
	@Override
	public final AccessMode getAccessMode() {
		return this.mode;
	}

	/** */
	@Override
	public final IsolationLevel getIsolationLevel() {
		return this.isolation;
	}

	/*
	 * protected final TransactionStatistics getStatistics() { return
	 * this.stats.clone(); }
	 */

	/** */
	protected final TransactionListener getListener() {
		return this.listener;
	}

	/**************************************************************************
	 * Setters
	 **************************************************************************/

	/** */
	protected final void setThread() {
		this.thread = Thread.currentThread();
	}

	/**************************************************************************
	 * Predicates
	 **************************************************************************/

	/** */
	@Override
	public final boolean isReader() {
		return this.mode == AccessMode.READ;
	}

	/** */
	@Override
	public final boolean isWriter() {
		return this.mode == AccessMode.WRITE;
	}

	/**************************************************************************
	 * Public Methods
	 **************************************************************************/

	/*
	 * @Override public final <T> void readPerformed(Resource<T> resource) {
	 * this.stats.incrementReadCount(resource); }
	 */

	/*
	 * @Override public final void writePerformed(String resource) {
	 * this.stats.putIfAbsent(resource);
	 * this.stats.incrementWriteCount(resource); }
	 */

	/** */
	@Override
	public final void interrupt() {
		this.thread.interrupt();
	}

	/** */
	protected final ResourceController newController() {
		return ResourceControllers.newController(this.mode);
	}

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
	public final boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || this.getClass() != o.getClass())
			return false;
		AbstractTransaction<?> n = (AbstractTransaction<?>) o;
		return this.id.equals(n.getId());
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
	public final int hashCode() {
		return this.id.hashCode();
	}

	/** Returns a string representation of the object. */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.id);
		return sb.toString();
	}

	/** Creates and returns a (deep) copy of this object. */
	@Override
	public abstract AbstractTransaction<T> clone();
}
