package com.github.undo4j.resources;

/**
 * ResourceWriteException
 * 
 * @author afs
 * @version 2013
 */

public class ResourceWriteException extends ResourceException {
	private static final long serialVersionUID = 1L;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Empty constructor of objects of class ResourceWriteException. */
	public ResourceWriteException() {
		super();
	}

	/** Parameter constructor of objects of class ResourceWriteException. */
	public ResourceWriteException(String message) {
		super(message);
	}

	/** Parameter constructor of objects of class ResourceWriteException. */
	public ResourceWriteException(String message, Throwable cause) {
		super(message, cause);
	}
}
