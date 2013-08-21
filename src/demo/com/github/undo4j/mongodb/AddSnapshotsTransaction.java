package com.github.undo4j.mongodb;

import java.util.ArrayList;
import java.util.List;

import com.github.undo4j.mongodb.dataaccess.SnapshotDAO;
import com.github.undo4j.mongodb.dataaccess.SystemDAO;
import com.github.undo4j.mongodb.datamodel.SnapshotObject;
import com.github.undo4j.mongodb.datamodel.SystemObject;
import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.resources.implementations.MongoCollectionInterface;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionalCallable;

class AddSnapshotsTransaction implements TransactionalCallable<Boolean> {
	/**
	 * 
	 */
	private ManagedResource<MongoCollectionInterface<SystemObject, SystemDAO>> systemResource;
	private ManagedResource<MongoCollectionInterface<SnapshotObject, SnapshotDAO>> snapshotResource;
	private MongoCollectionInterface<SystemObject, SystemDAO> systems;
	private MongoCollectionInterface<SnapshotObject, SnapshotDAO> snapshots;

	public AddSnapshotsTransaction(MongoCollectionInterface<SystemObject, SystemDAO> systems,
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
		systemResource.write(new ImmutableState<MongoCollectionInterface<SystemObject, SystemDAO>>(systems));
		snapshotResource.write(new ImmutableState<MongoCollectionInterface<SnapshotObject, SnapshotDAO>>(snapshots));
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