package org.bitbucket.jtransaction.sshrepo;

import java.io.PrintStream;

import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;

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

	private PrintStream out;

	public CommandIssuer(PrintStream ch) {
		this.out = ch;
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
		out.println(state.get());
	}
}
