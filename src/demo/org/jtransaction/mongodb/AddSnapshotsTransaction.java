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

class AddSnapshotsTransaction implements TransactionalCallable<Boolean> {
	/**
	 * 
	 */
	private ManagedResource<MongoCollection<SystemObject, SystemDAO>> systemResource;
	private ManagedResource<MongoCollection<SnapshotObject, SnapshotDAO>> snapshotResource;
	private MongoCollection<SystemObject, SystemDAO> systems;
	private MongoCollection<SnapshotObject, SnapshotDAO> snapshots;

	public AddSnapshotsTransaction(
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
		systemResource
				.write(new ImmutableState<MongoCollection<SystemObject, SystemDAO>>(
						systems));
		snapshotResource
				.write(new ImmutableState<MongoCollection<SnapshotObject, SnapshotDAO>>(
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