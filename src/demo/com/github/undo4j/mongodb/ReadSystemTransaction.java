package com.github.undo4j.mongodb;

import java.util.ArrayList;
import java.util.List;

import com.github.undo4j.mongodb.dataaccess.SystemDAO;
import com.github.undo4j.mongodb.datamodel.SystemObject;
import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.resources.implementations.MongoCollectionInterface;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionalCallable;

class ReadSystemTransaction implements TransactionalCallable<Boolean> {
	/**
	 * 
	 */
	private ManagedResource<MongoCollectionInterface<SystemObject, SystemDAO>> systemResource;
	private MongoCollectionInterface<SystemObject, SystemDAO> system;

	public ReadSystemTransaction(MongoCollectionInterface<SystemObject, SystemDAO> system,
			ManagedResource<MongoCollectionInterface<SystemObject, SystemDAO>> systemResource) {
		this.systemResource = systemResource;
		this.system = system;
	}

	@Override
	public Boolean call() throws Exception {
		systemResource.write(new ImmutableState<>(system));
		return new Boolean(true);
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> list = new ArrayList<>();
		list.add(systemResource);
		return list;
	}

}