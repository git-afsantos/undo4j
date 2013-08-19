package org.bitbucket.jtransaction.resources.implementations;

import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;

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
