package org.jtransaction.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.jtransaction.resources.ImmutableState;
import org.bitbucket.jtransaction.resources.implementations.MongoCollectionInterface;
import org.bitbucket.jtransaction.transactions.ManagedResource;
import org.bitbucket.jtransaction.transactions.TransactionalCallable;
import org.jtransaction.mongodb.dataaccess.SnapshotDAO;
import org.jtransaction.mongodb.dataaccess.SystemDAO;
import org.jtransaction.mongodb.datamodel.SnapshotObject;
import org.jtransaction.mongodb.datamodel.SystemObject;

class AddSnapshotsTransaction implements TransactionalCallable<Boolean> {
	/**
	 * 
	 */
	private ManagedResource<MongoCollectionInterface<SystemObject, SystemDAO>> systemResource;
	private ManagedResource<MongoCollectionInterface<SnapshotObject, SnapshotDAO>> snapshotResource;
	private MongoCollectionInterface<SystemObject, SystemDAO> systems;
	private MongoCollectionInterface<SnapshotObject, SnapshotDAO> snapshots;

	public AddSnapshotsTransaction(
			MongoCollectionInterface<SystemObject, SystemDAO> systems,
			MongoCollectionInterface<SnapshotObject, SnapshotDAO> snapshots,
			ManagedResource<MongoCollectionInterface<SystemObject, SystemDAO>> systemResource,
			ManagedResource<MongoCollectionInterface<SnapshotObject, SnapshotDAO>> snapshotResource) {
		this.systemResource = systemResource;
		this.snapshotResource = snapshotResource;
		this.systems = systems;
		this.snapshots = snapshots;
	}

	@Override
	public Boolean call() throws Exception {
		systemResource
				.write(new ImmutableState<MongoCollectionInterface<SystemObject, SystemDAO>>(
						systems));
		snapshotResource
				.write(new ImmutableState<MongoCollectionInterface<SnapshotObject, SnapshotDAO>>(
						snapshots));
		return new Boolean(true);
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> list = new ArrayList<>();
		list.add(systemResource);
		list.add(snapshotResource);
		return list;
	}

}