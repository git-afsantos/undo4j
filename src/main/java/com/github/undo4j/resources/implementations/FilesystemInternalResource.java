package com.github.undo4j.resources.implementations;

import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.ResourceState;

public class FilesystemInternalResource implements
		InternalResource<FilesystemInterface> {

	@Override
	public boolean isValidState(ResourceState<FilesystemInterface> state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResourceState<FilesystemInterface> buildState() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyState(ResourceState<FilesystemInterface> state)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
