package org.bitbucket.jtransaction.resources.implementations;

import java.util.List;

public interface MongoDAO<T> {

	public T readObject(T object);

	public List<T> readObjects(List<T> objects);

	public void writeObject(T object);

	public void writeObjects(List<T> objects);

	public void deleteObject(T object);

	public void deleteObjects(List<T> objects);

}
