package org.bitbucket.jtransaction.sshrepo;

import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;

public final class CommandIssuer implements InternalResource<String> {

	@Override
	public boolean isValidState(ResourceState<String> state) {
		// Validate a command, maybe?
		return true;
	}

	@Override
	public ResourceState<String> buildState() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyState(ResourceState<String> state) throws Exception {
		// Applies the given command on ssh-exec

	}

}
