package org.bitbucket.jtransaction.resources.implementations;

import org.bitbucket.jtransaction.common.LockManager;
import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;
import org.bitbucket.jtransaction.resources.StatelessResource;
import org.jtransaction.mongodb.MongoCollection;

public class MongoResource<T, D extends MongoDAO<T>> extends
		StatelessResource<MongoCollection<T, D>> {

	public MongoResource(InternalResource<MongoCollection<T, D>> resource,
			LockManager lockManager) {
		super(resource, lockManager);
	}

	@Override
	public void rollback() {
		super.rollback();
		InternalResource<MongoCollection<T, D>> internalResource = getInternalResource();
		try {
			ResourceState<MongoCollection<T, D>> resourceState = internalResource
					.buildState();
			MongoCollection<T, D> dataObject = resourceState.get();
			dataObject.rollback();
		} catch (Exception e) {
			throw new RuntimeException("rollback failed", e);
		}

	}
}
