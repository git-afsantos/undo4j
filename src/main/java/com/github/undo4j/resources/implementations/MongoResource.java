package com.github.undo4j.resources.implementations;

import com.github.undo4j.common.LockManager;
import com.github.undo4j.common.LockManagers;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.ResourceState;
import com.github.undo4j.resources.StatelessResource;

public class MongoResource<T, D extends MongoDAO<T>> extends
		StatelessResource<MongoCollectionInterface<T, D>> {

	public MongoResource(InternalResource<MongoCollectionInterface<T, D>> resource,
			LockManager lockManager) {
		super(resource, lockManager);
	}

	public MongoResource(InternalResource<MongoCollectionInterface<T, D>> resource) {
		super(resource, LockManagers.newNullLockManager());
	}

	@Override
	public void rollback() {
		super.rollback();
		InternalResource<MongoCollectionInterface<T, D>> internalResource = getInternalResource();
		try {
			ResourceState<MongoCollectionInterface<T, D>> resourceState = internalResource
					.buildState();
			MongoCollectionInterface<T, D> dataObject = resourceState.get();
			dataObject.rollback();
		} catch (Exception e) {
			throw new RuntimeException("rollback failed", e);
		}

	}
}
