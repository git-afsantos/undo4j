package org.bitbucket.jtransaction.resources.implementations;

public enum Action {

	WRITE, READ, DELETE;

	public static Action getDefaultAction() {
		return WRITE;
	}
}
