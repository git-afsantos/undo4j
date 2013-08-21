package com.github.undo4j.resources;

/**
 * ResourceCommitException
 * 
 * @author afs
 * @version 2013
 */

public class ResourceCommitException extends ResourceException {
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class ResourceCommitException. */
	public ResourceCommitException() {
		super();
	}

	/** Parameter constructor of objects of class ResourceCommitException. */
	public ResourceCommitException(String message) {
		super(message);
	}

	/** Parameter constructor of objects of class ResourceCommitException. */
	public ResourceCommitException(String message, Throwable cause) {
		super(message, cause);
	}
}
