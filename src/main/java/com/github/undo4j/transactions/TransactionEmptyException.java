package com.github.undo4j.transactions;

/**
 * TransactionEmptyException
 * 
 * @author afs
 * @version 2013
 */

public final class TransactionEmptyException extends TransactionException {
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class TransactionEmptyException. */
	TransactionEmptyException() {
		super();
	}

	/** Parameter constructor of objects of class TransactionEmptyException. */
	TransactionEmptyException(String message) {
		super(message);
	}
}
