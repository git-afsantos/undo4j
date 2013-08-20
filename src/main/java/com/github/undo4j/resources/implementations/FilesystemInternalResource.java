package com.github.undo4j.resources.implementations;

import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.ResourceState;

public class FilesystemInternalResource implements
		InternalResource<FilesystemInterface> {

	protected FilesystemInterface dataObject;

	@Override
	public boolean isValidState(ResourceState<FilesystemInterface> state) {
		return !state.isNull();

	}

	@Override
	public ResourceState<FilesystemInterface> buildState() throws Exception {
		return new ImmutableState<FilesystemInterface>(dataObject);

	}

	@Override
	public void applyState(ResourceState<FilesystemInterface> state)
			throws Exception {
		dataObject = state.get();
		dataObject.run();
	}

}
