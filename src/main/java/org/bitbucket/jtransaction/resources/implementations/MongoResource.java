package org.bitbucket.jtransaction.resources.implementations;

import org.bitbucket.jtransaction.common.LockManager;
import org.bitbucket.jtransaction.common.LockManagers;
import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;
import org.bitbucket.jtransaction.resources.StatelessResource;

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
