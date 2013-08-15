package org.bitbucket.jtransaction.sshrepo;

import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.bitbucket.jtransaction.sshrepo.ScpFrom.MyUserInfo;
import org.bitbucket.jtransaction.transactions.TransactionManager;
import org.bitbucket.jtransaction.transactions.TransactionManagers;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public final class ScpMain {
	public static void main(String[] args) throws JSchException, IOException,
			InterruptedException, ExecutionException {
		if (args.length != 2) {
			System.out.println("usage: java jcp src user@host:dest");
			System.exit(-1);
		}

		String user, host;

		// src = args[0];
		user = args[1].substring(0, args[1].indexOf('@'));
		args[1] = args[1].substring(args[1].indexOf('@') + 1);
		host = args[1].substring(0, args[1].indexOf(':'));
		// dest = args[1].substring(args[1].indexOf(':') + 1);

		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, 22);
		UserInfo ui = new MyUserInfo();
		session.setUserInfo(ui);
		session.connect();

		ChannelShell channel = (ChannelShell) session.openChannel("shell");

		PrintStream out = new PrintStream(channel.getOutputStream(), true);
		channel.connect();

		CommandIssuer cmd = new CommandIssuer(out);

		ScpResource res = new ScpResource(cmd);
		ScpTransaction tr = new ScpTransaction(res, args[0], args[1]);
		TransactionManager man = TransactionManagers.newSynchronousManager();
		Future<Object> f = man.submit(tr);

		f.get();

		man.shutdown();

		channel.disconnect();
		session.disconnect();
	}
}
