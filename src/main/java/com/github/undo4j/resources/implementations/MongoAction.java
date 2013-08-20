package com.github.undo4j.resources.implementations;

public enum MongoAction {

	WRITE, READ, DELETE;

	public static MongoAction getDefaultAction() {
		return WRITE;
	}
}
