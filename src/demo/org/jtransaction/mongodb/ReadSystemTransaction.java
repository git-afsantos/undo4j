package org.jtransaction.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.jtransaction.resources.ImmutableState;
import org.bitbucket.jtransaction.resources.implementations.MongoCollectionInterface;
import org.bitbucket.jtransaction.transactions.ManagedResource;
import org.bitbucket.jtransaction.transactions.TransactionalCallable;
import org.jtransaction.mongodb.dataaccess.SystemDAO;
import org.jtransaction.mongodb.datamodel.SystemObject;

class ReadSystemTransaction implements TransactionalCallable<Boolean> {
	/**
	 * 
	 */
	private ManagedResource<MongoCollectionInterface<SystemObject, SystemDAO>> systemResource;
	private MongoCollectionInterface<SystemObject, SystemDAO> system;

	public ReadSystemTransaction(
			MongoCollectionInterface<SystemObject, SystemDAO> system,
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