package org.bitbucket.jtransaction.sshrepo;

import java.io.InputStream;

import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public final class CommandIssuer implements InternalResource<String> {
	public static class CommandIssuerException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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

	public CommandIssuer(ChannelExec ch) throws CommandIssuerException,
			JSchException {
		this.channel = ch;
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
		InputStream in;
		byte[] tmp = new byte[1024];

		channel.setCommand(state.get());
		in = channel.getInputStream();
		channel.connect();

		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				System.out.print(new String(tmp, 0, i));
			}
			if (channel.isClosed()) {
				System.out.println("exit-status: " + channel.getExitStatus());
				break;
			}
		}
		channel.disconnect();
	}
}
