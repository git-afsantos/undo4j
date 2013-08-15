package org.bitbucket.jtransaction.sshrepo;

import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;

import com.jcraft.jsch.ChannelExec;
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

	private Session session;

	public CommandIssuer(Session s) {
		this.session = s;
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
		if (state.isNull()) {
			return;
		}
		ChannelExec channel = (ChannelExec) session.openChannel("exec");
		channel.setCommand(state.get());

		System.out.println("Running> " + state.get());

		((ChannelExec) channel).setErrStream(System.err);
		((ChannelExec) channel).setOutputStream(System.out);
		java.io.InputStream in = channel.getInputStream();
		channel.connect();

		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				System.out.print(new String(tmp, 0, i));
			}
			if (channel.isClosed()) {
				if (channel.getExitStatus() > 0) {
					channel.disconnect();
					throw new Exception("Must rollback!");
				} else
					break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}
		}

		channel.disconnect();
	}
}
