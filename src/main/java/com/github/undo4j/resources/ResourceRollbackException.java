package com.github.undo4j.resources;

/**
 * ResourceRollbackException
 * 
 * @author afs
 * @version 2013
 */

public class ResourceRollbackException extends ResourceException {
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class ResourceRollbackException. */
	public ResourceRollbackException() {
		super();
	}

	/** Parameter constructor of objects of class ResourceRollbackException. */
	public ResourceRollbackException(String message) {
		super(message);
	}

	/** Parameter constructor of objects of class ResourceRollbackException. */
	public ResourceRollbackException(String message, Throwable cause) {
		super(message, cause);
	}
}
