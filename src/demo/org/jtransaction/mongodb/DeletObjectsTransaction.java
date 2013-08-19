package org.jtransaction.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.jtransaction.resources.ImmutableState;
import org.bitbucket.jtransaction.transactions.ManagedResource;
import org.bitbucket.jtransaction.transactions.TransactionalCallable;
import org.jtransaction.mongodb.dataaccess.SnapshotDAO;
import org.jtransaction.mongodb.dataaccess.SystemDAO;
import org.jtransaction.mongodb.datamodel.SnapshotObject;
import org.jtransaction.mongodb.datamodel.SystemObject;

public class DeletObjectsTransaction implements TransactionalCallable<Boolean> {

	private ManagedResource<MongoCollection<SystemObject, SystemDAO>> systemResource;
	private ManagedResource<MongoCollection<SnapshotObject, SnapshotDAO>> snapshotResource;
	private MongoCollection<SystemObject, SystemDAO> systems;
	private MongoCollection<SnapshotObject, SnapshotDAO> snapshots;

	public DeletObjectsTransaction(
			MongoCollection<SystemObject, SystemDAO> systems,
			MongoCollection<SnapshotObject, SnapshotDAO> snapshots,
			ManagedResource<MongoCollection<SystemObject, SystemDAO>> systemResource,
			ManagedResource<MongoCollection<SnapshotObject, SnapshotDAO>> snapshotResource) {
		this.systemResource = systemResource;
		this.snapshotResource = snapshotResource;
		this.systems = systems;
		this.snapshots = snapshots;
	}

	@Override
	public Boolean call() throws Exception {
		snapshotResource
				.write(new ImmutableState<MongoCollection<SnapshotObject, SnapshotDAO>>(
						snapshots));
		systemResource
				.write(new ImmutableState<MongoCollection<SystemObject, SystemDAO>>(
						systems));
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
