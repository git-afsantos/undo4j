package org.bitbucket.jtransaction.resources.implementations;

public enum MongoAction {

	WRITE, READ, DELETE;

	public static MongoAction getDefaultAction() {
		return WRITE;
	}
}
