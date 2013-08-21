package com.github.undo4j.transactions;

import com.github.undo4j.resources.Resource;

public abstract class ResourceListenerAdapter implements ResourceListener {

	@Override
	public <T> void readCalled(Resource<T> resource) {
	}

	@Override
	public <T> void writeCalled(Resource<T> resource) {
	}

	@Override
	public <T> void readPerformed(Resource<T> resource) {
	}

	@Override
	public <T> void writePerformed(Resource<T> resource) {
	}

	@Override
	public <T> void commitCalled(Resource<T> resource) {
	}

	@Override
	public <T> void rollbackCalled(Resource<T> resource) {
	}

	@Override
	public <T> void updateCalled(Resource<T> resource) {
	}

	@Override
	public <T> void commitPerformed(Resource<T> resource) {
	}

	@Override
	public <T> void rollbackPerformed(Resource<T> resource) {
	}

	@Override
	public <T> void updatePerformed(Resource<T> resource) {
	}
}
