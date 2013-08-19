package org.jtransaction.mongodb;

import org.bitbucket.jtransaction.resources.ResourceState;
import org.bitbucket.jtransaction.resources.implementations.MongoCollectionInterface;
import org.bitbucket.jtransaction.resources.implementations.MongoInternalResource;
import org.jtransaction.mongodb.dataaccess.SnapshotDAO;
import org.jtransaction.mongodb.datamodel.SnapshotObject;

public class SnapshotResource extends
		MongoInternalResource<SnapshotObject, SnapshotDAO> {

	@Override
	public ResourceState<MongoCollectionInterface<SnapshotObject, SnapshotDAO>> buildState()
			throws Exception {
		return super.buildState();
	}

	@Override
	public void applyState(
			ResourceState<MongoCollectionInterface<SnapshotObject, SnapshotDAO>> state)
			throws Exception {
		super.applyState(state);
		dataObject.run();
	}

}
