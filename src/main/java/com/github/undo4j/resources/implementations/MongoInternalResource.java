package com.github.undo4j.resources.implementations;

import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.ResourceState;

public abstract class MongoInternalResource<T, D extends MongoDAO<T>> implements
		InternalResource<MongoCollectionInterface<T, D>> {
	protected MongoCollectionInterface<T, D> dataObject;

	@Override
	public boolean isValidState(ResourceState<MongoCollectionInterface<T, D>> state) {
		return !state.isNull();
	}

	@Override
	public ResourceState<MongoCollectionInterface<T, D>> buildState() throws Exception {
		return new ImmutableState<MongoCollectionInterface<T, D>>(dataObject);
	}

	@Override
	public void applyState(ResourceState<MongoCollectionInterface<T, D>> state) throws Exception {
		dataObject = state.get();
	}
}
