package com.github.undo4j.resources;

/**
 * ResourceException
 * 
 * @author afs
 * @version 2013
 */

public abstract class ResourceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class ResourceException. */
	public ResourceException() {
		super();
	}

	/** Parameter constructor of objects of class ResourceException. */
	public ResourceException(String message) {
		super(message);
	}

	/** Parameter constructor of objects of class ResourceException. */
	public ResourceException(String message, Throwable cause) {
		super(message, cause);
	}
}
