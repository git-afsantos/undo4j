package com.github.undo4j.resources;

/**
 * ResourceUpdateException
 * 
 * @author afs
 * @version 2013
 */

public class ResourceUpdateException extends ResourceException {
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class ResourceUpdateException. */
	public ResourceUpdateException() {
		super();
	}

	/** Parameter constructor of objects of class ResourceUpdateException. */
	public ResourceUpdateException(String message) {
		super(message);
	}

	/** Parameter constructor of objects of class ResourceUpdateException. */
	public ResourceUpdateException(String message, Throwable cause) {
		super(message, cause);
	}
}
