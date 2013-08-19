package org.jtransaction.mongodb;

import org.bitbucket.jtransaction.resources.ResourceState;
import org.bitbucket.jtransaction.resources.implementations.MongoCollectionInterface;
import org.bitbucket.jtransaction.resources.implementations.MongoInternalResource;
import org.jtransaction.mongodb.dataaccess.SystemDAO;
import org.jtransaction.mongodb.datamodel.SystemObject;

public class SystemResource extends
		MongoInternalResource<SystemObject, SystemDAO> {

	@Override
	public ResourceState<MongoCollectionInterface<SystemObject, SystemDAO>> buildState()
			throws Exception {
		return super.buildState();
	}

	@Override
	public void applyState(
			ResourceState<MongoCollectionInterface<SystemObject, SystemDAO>> state)
			throws Exception {
		super.applyState(state);
		dataObject.run();
	}
}
