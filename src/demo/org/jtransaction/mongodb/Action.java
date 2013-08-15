package org.jtransaction.mongodb;

public enum Action {

	WRITE, READ, DELETE;

	public static Action getDefaultAction() {
		return WRITE;
	}
}
