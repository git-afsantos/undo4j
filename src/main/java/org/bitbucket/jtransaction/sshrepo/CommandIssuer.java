package org.bitbucket.jtransaction.sshrepo;

import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public final class CommandIssuer implements InternalResource<String> {
	public static class CommandIssuerException extends Exception {
		public CommandIssuerException(String msg) {
			super(msg);
		}
	}

	private ChannelExec channel;

	/*
	 * Receives an active (connected) session to create an ChannelExec.
	 */
	public CommandIssuer(Session active) throws CommandIssuerException,
			JSchException {
		if (!active.isConnected())
			throw new CommandIssuerException("Must receive an active session");

		this.channel = (ChannelExec) active.openChannel("exec");
	}

	@Override
	public boolean isValidState(ResourceState<String> state) {
		// Validate a command, maybe?
		return true;
	}

	@Override
	public ResourceState<String> buildState() throws Exception {
		return null;
	}

	@Override
	public void applyState(ResourceState<String> state) throws Exception {
		channel.setCommand(state.get());
	}
}
