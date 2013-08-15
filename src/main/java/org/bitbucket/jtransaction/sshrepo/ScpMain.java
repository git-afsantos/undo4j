package org.bitbucket.jtransaction.sshrepo;

import org.bitbucket.jtransaction.transactions.TransactionManager;
import org.bitbucket.jtransaction.transactions.TransactionManagers;

import com.jcraft.jsch.ChannelExec;

public final class ScpMain {
	public static void main(String[] args) {
		CommandIssuer cmd = new CommandIssuer((ChannelExec) null);
		ScpResource res = new ScpResource(cmd);
		ScpTransaction tr = new ScpTransaction(res, args[0], args[1]);
		TransactionManager man = TransactionManagers.newSynchronousManager();
		man.submit(tr);
		man.shutdown();
	}
}
