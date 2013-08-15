package org.jtransaction.mongodb;

import java.util.List;

import org.bitbucket.jtransaction.resources.implementations.MongoDAO;

public class MongoCollection<T, D extends MongoDAO<T>> {

	private D dao;
	private List<T> objects;
	private Action action;

	public MongoCollection(D dao, List<T> objects, Action action) {
		this.dao = dao;
		this.objects = objects;
		this.action = action;
	}

	public void rollback() {
		switch (action) {
		case READ:
			break;
		case WRITE:
			action = Action.DELETE;
			break;
		case DELETE:
			throw new RuntimeException("Rollback Delete not yet implemented");
		}
		run();
	}

	public void run() {
		switch (action) {
		case READ:
			readObjects();
			break;
		case WRITE:
			writeObjects();
			break;
		case DELETE:
			deletObjects();
			break;
		}

	}

	public List<T> getObjects() {
		return objects;
	}

	private void deletObjects() {
		dao.deleteObjects(objects);
	}

	private void writeObjects() {
		dao.writeObjects(objects);
	}

	private void readObjects() {
		objects = dao.readObjects(objects);
	}
}
