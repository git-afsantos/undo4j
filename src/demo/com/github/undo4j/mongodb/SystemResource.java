package com.github.undo4j.mongodb;

import com.github.undo4j.mongodb.dataaccess.SystemDAO;
import com.github.undo4j.mongodb.datamodel.SystemObject;
import com.github.undo4j.resources.ResourceState;
import com.github.undo4j.resources.implementations.MongoCollectionInterface;
import com.github.undo4j.resources.implementations.MongoInternalResource;

public class SystemResource extends MongoInternalResource<SystemObject, SystemDAO> {

	@Override
	public ResourceState<MongoCollectionInterface<SystemObject, SystemDAO>> buildState() throws Exception {
		return super.buildState();
	}

	@Override
	public void applyState(ResourceState<MongoCollectionInterface<SystemObject, SystemDAO>> state) throws Exception {
		super.applyState(state);
		dataObject.run();
	}
}
