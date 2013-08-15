package org.jtransaction.mongodb;

import org.bitbucket.jtransaction.resources.ResourceState;
import org.bitbucket.jtransaction.resources.implementations.MongoInternalResource;
import org.jtransaction.mongodb.dataaccess.SystemDAO;
import org.jtransaction.mongodb.datamodel.SystemObject;

public class SystemResource extends
		MongoInternalResource<SystemObject, SystemDAO> {

	private final SystemDAO dao;

	public SystemResource(SystemDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public ResourceState<MongoCollection<SystemObject, SystemDAO>> buildState()
			throws Exception {
		return super.buildState();
	}

	@Override
	public void applyState(
			ResourceState<MongoCollection<SystemObject, SystemDAO>> state)
			throws Exception {
		super.applyState(state);
		dataObject.run();
	}

}
