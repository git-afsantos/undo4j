package com.github.undo4j.transactions;

/**
 * TransactionInterruptedException
 * 
 * @author afs
 * @version 2013
 */

public final class TransactionInterruptedException extends TransactionException {
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class TransactionInterruptedException. */
	TransactionInterruptedException() {
		super();
	}

	/**
	 * Parameter constructor of objects of class
	 * TransactionInterruptedException.
	 */
	TransactionInterruptedException(String message) {
		super(message);
	}

	/**
	 * Parameter constructor of objects of class
	 * TransactionInterruptedException.
	 */
	TransactionInterruptedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Parameter constructor of objects of class
	 * TransactionInterruptedException.
	 */
	TransactionInterruptedException(Throwable cause) {
		super(cause);
	}
}
