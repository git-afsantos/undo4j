package org.bitbucket.jtransaction.resources.implementations;

import org.bitbucket.jtransaction.resources.ImmutableState;
import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;
import org.jtransaction.mongodb.MongoCollection;

public abstract class MongoInternalResource<T, D extends MongoDAO<T>>
		implements InternalResource<MongoCollection<T, D>> {
	protected MongoCollection<T, D> dataObject;

	@Override
	public boolean isValidState(ResourceState<MongoCollection<T, D>> state) {
		return !state.isNull();
	}

	@Override
	public ResourceState<MongoCollection<T, D>> buildState() throws Exception {
		return new ImmutableState<MongoCollection<T, D>>(dataObject);
	}

	@Override
	public void applyState(ResourceState<MongoCollection<T, D>> state)
			throws Exception {
		dataObject = state.get();
	}
}
