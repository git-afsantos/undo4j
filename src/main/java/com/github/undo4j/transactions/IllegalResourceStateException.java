package com.github.undo4j.transactions;

public final class IllegalResourceStateException extends IllegalStateException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalResourceStateException() {
		super();
	}

	public IllegalResourceStateException(String message) {
		super(message);
	}

	public IllegalResourceStateException(String message, Throwable cause) {
		super(message, cause);
	}
}
