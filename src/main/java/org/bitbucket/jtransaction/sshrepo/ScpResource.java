package org.bitbucket.jtransaction.sshrepo;

import org.bitbucket.jtransaction.common.LockManagers;
import org.bitbucket.jtransaction.resources.StatelessResource;

public final class ScpResource extends StatelessResource<String> {
	// private flags...
	// SSH Session

	public ScpResource() {
		super(new CommandIssuer(), LockManagers.newNullLockManager());
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public void initialize() {
		// Connect etc.
	}

	/**
	 * 
	 */
	public void dispose() {
		// Close connection etc.
	}

	/**
	 * Removes the old renamed folder on the target server.
	 */
	@Override
	public void commit() {

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

	}
}
