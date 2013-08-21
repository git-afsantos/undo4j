package com.github.undo4j.mongodb;

import com.github.undo4j.mongodb.dataaccess.SnapshotDAO;
import com.github.undo4j.mongodb.datamodel.SnapshotObject;
import com.github.undo4j.resources.ResourceState;
import com.github.undo4j.resources.implementations.MongoCollectionInterface;
import com.github.undo4j.resources.implementations.MongoInternalResource;

public class SnapshotResource extends MongoInternalResource<SnapshotObject, SnapshotDAO> {

	@Override
	public ResourceState<MongoCollectionInterface<SnapshotObject, SnapshotDAO>> buildState() throws Exception {
		return super.buildState();
	}

	@Override
	public void applyState(ResourceState<MongoCollectionInterface<SnapshotObject, SnapshotDAO>> state) throws Exception {
		super.applyState(state);
		dataObject.run();
	}

}
