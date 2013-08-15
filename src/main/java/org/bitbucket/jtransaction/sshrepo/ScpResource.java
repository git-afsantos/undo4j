package org.bitbucket.jtransaction.sshrepo;

import org.bitbucket.jtransaction.common.LockManagers;
import org.bitbucket.jtransaction.resources.ResourceCommitException;
import org.bitbucket.jtransaction.resources.ResourceRollbackException;
import org.bitbucket.jtransaction.resources.ResourceState;
import org.bitbucket.jtransaction.resources.StatelessResource;

public final class ScpResource extends StatelessResource<String> {
	// private flags...
	private ResourceState<String> commitCmd;
	private ResourceState<String> rollbackCmd;

	public ScpResource(CommandIssuer cmd, ResourceState<String> commitCmd,
			ResourceState<String> rollbackCmd) {
		super(cmd, LockManagers.newNullLockManager());
		this.commitCmd = commitCmd;
		this.rollbackCmd = rollbackCmd;
	}

	/**
	 * Removes the old renamed folder on the target server.
	 */
	@Override
	public void commit() {
		try {
			this.getInternalResource().applyState(commitCmd);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceCommitException(e.getMessage(), e);
		}
	}

	/**
	 *
	 */
	/*
	 * @Override public void update() {
	 * 
	 * }
	 */

	/**
	 * Failed on renaming target directory: return. Failed on copying directory:
	 * Delete what was copied; restore old name. Failed on deleting old
	 * directory: retry deleting.
	 */
	@Override
	public void rollback() {
		try {
			this.getInternalResource().applyState(commitCmd);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceRollbackException(e.getMessage(), e);
		}
	}
}
