package org.jtransaction.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.jtransaction.resources.implementations.MongoDAO;

public class MongoCollection<T, D extends MongoDAO<T>> {

	private D dao;
	private List<T> objects;
	private Action action;
	private List<T> objectsChanged;

	public MongoCollection(D dao, List<T> objects, Action action) {
		this.dao = dao;
		this.objects = objects;
		this.action = action;
		this.objectsChanged = new ArrayList<T>(objects.size());
	}

	public void rollback() {
		switch (action) {
		case READ:
			break;
		case WRITE:
			action = Action.DELETE;
			break;
		case DELETE:
			action = Action.WRITE;
			objects = objectsChanged;
			break;
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
		List<T> deletedObjects = new ArrayList<>(objects.size());
		try {
			for (T object : objects) {
				dao.deleteObject(object);
				deletedObjects.add(object);
			}
		} finally {
			objectsChanged = deletedObjects;
		}

	}

	private void writeObjects() {
		List<T> writtenObjects = new ArrayList<>(objects.size());
		try {
			for (T object : objects) {
				dao.writeObject(object);
				writtenObjects.add(object);
			}
		} finally {
			objectsChanged = writtenObjects;
		}
	}

	private void readObjects() {
		objects = dao.readObjects(objects);
	}

}
