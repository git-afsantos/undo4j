package org.bitbucket.jtransaction.resources.implementations;

import org.bitbucket.jtransaction.resources.ImmutableState;
import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;

import com.github.jmkgreen.morphia.dao.BasicDAO;

public class MongoResource<T, K> implements InternalResource<T> {
	private BasicDAO<T, K> dao;
	private T dataObject;

	@Override
	public boolean isValidState(ResourceState<T> state) {
		return !state.isNull();
	}

	@Override
	public ResourceState<T> buildState() throws Exception {
		return new ImmutableState<T>(dataObject);
	}

	@Override
	public void applyState(ResourceState<T> state) throws Exception {

	}

}
