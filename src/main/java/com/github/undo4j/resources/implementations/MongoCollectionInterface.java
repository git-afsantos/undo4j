package com.github.undo4j.resources.implementations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MongoCollectionInterface<T, D extends MongoDAO<T>> {

	private D dao;
	private List<T> objects;
	private MongoAction action;
	private List<T> objectsChanged;

	public MongoCollectionInterface(D dao, List<T> objects, MongoAction action) {
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
			action = MongoAction.DELETE;
			break;
		case DELETE:
			action = MongoAction.WRITE;
			objects = objectsChanged;
			break;
		}
		run();
		this.objectsChanged = new LinkedList<T>();
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