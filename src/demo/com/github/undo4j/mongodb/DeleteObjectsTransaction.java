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

public class DeleteObjectsTransaction implements TransactionalCallable<String> {

	private ManagedResource<MongoCollectionInterface<SystemObject, SystemDAO>> systemResource;
	private ManagedResource<MongoCollectionInterface<SnapshotObject, SnapshotDAO>> snapshotResource;
	private MongoCollectionInterface<SystemObject, SystemDAO> systems;
	private MongoCollectionInterface<SnapshotObject, SnapshotDAO> snapshots;

	public DeleteObjectsTransaction(MongoCollectionInterface<SystemObject, SystemDAO> systems,
			MongoCollectionInterface<SnapshotObject, SnapshotDAO> snapshots,
			ManagedResource<MongoCollectionInterface<SystemObject, SystemDAO>> systemResource,
			ManagedResource<MongoCollectionInterface<SnapshotObject, SnapshotDAO>> snapshotResource) {
		this.systemResource = systemResource;
		this.snapshotResource = snapshotResource;
		this.systems = systems;
		this.snapshots = snapshots;
	}

	@Override
	public String call() throws Exception {
		snapshotResource.write(new ImmutableState<MongoCollectionInterface<SnapshotObject, SnapshotDAO>>(snapshots));
		systemResource.write(new ImmutableState<MongoCollectionInterface<SystemObject, SystemDAO>>(systems));

		return "Objects deleted. Transaction will commit.";
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> list = new ArrayList<>();
		list.add(systemResource);
		list.add(snapshotResource);
		return list;
	}

}
