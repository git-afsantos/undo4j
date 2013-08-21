package com.github.undo4j.resources;

/**
 * ResourceReleaseException
 * 
 * @author afs
 * @version 2013
 */

public class ResourceReleaseException extends ResourceException {
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class ResourceReleaseException. */
	public ResourceReleaseException() {
		super();
	}

	/** Parameter constructor of objects of class ResourceReleaseException. */
	public ResourceReleaseException(String message) {
		super(message);
	}

	/** Parameter constructor of objects of class ResourceReleaseException. */
	public ResourceReleaseException(String message, Throwable cause) {
		super(message, cause);
	}
}
