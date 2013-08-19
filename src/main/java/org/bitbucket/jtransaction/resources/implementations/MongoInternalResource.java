package org.bitbucket.jtransaction.resources.implementations;

import org.bitbucket.jtransaction.resources.ImmutableState;
import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;

public abstract class MongoInternalResource<T, D extends MongoDAO<T>>
		implements InternalResource<MongoCollectionInterface<T, D>> {
	protected MongoCollectionInterface<T, D> dataObject;

	@Override
	public boolean isValidState(
			ResourceState<MongoCollectionInterface<T, D>> state) {
		return !state.isNull();
	}

	@Override
	public ResourceState<MongoCollectionInterface<T, D>> buildState()
			throws Exception {
		return new ImmutableState<MongoCollectionInterface<T, D>>(dataObject);
	}

	@Override
	public void applyState(ResourceState<MongoCollectionInterface<T, D>> state)
			throws Exception {
		dataObject = state.get();
	}
}
