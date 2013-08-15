package org.bitbucket.jtransaction.sshrepo;

import java.util.LinkedList;
import java.util.List;

import org.bitbucket.jtransaction.resources.NormalState;
import org.bitbucket.jtransaction.resources.NullState;
import org.bitbucket.jtransaction.resources.ResourceState;
import org.bitbucket.jtransaction.transactions.ManagedResource;
import org.bitbucket.jtransaction.transactions.TransactionalCallable;

public class ScpTransaction implements TransactionalCallable<Object> {
	private ManagedResource<String> resource;
	private ScpResource scpResource;
	private String sourceDir, targetDir;

	public ScpTransaction(ScpResource r, String s, String t) {
		scpResource = r;
		resource = ManagedResource.from(r);
		sourceDir = s;
		targetDir = t;
	}

	@Override
	public Object call() throws Exception {
		String stampedName = new StringBuilder().append(targetDir)
				.append(System.currentTimeMillis()).toString();

		makeRemoteDirectory();
		renameRemoteDirectory(stampedName);

		scpResource.setCommitCmd(deleteAfterCommit(stampedName));
		scpResource.setRollbackCmd(rollbackOnCopying(stampedName));

		copyDirectory();
		scpResource.setRollbackCmd(new NullState<String>());

		return null;
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> list = new LinkedList<>();
		list.add(resource);
		return list;
	}

	private void renameRemoteDirectory(String name) {
		String cmd = new StringBuilder("mv ")
				.append(trimRemoteDirectory(targetDir)).append(' ')
				.append(trimRemoteDirectory(name)).toString();
		resource.write(stringToState(cmd));
	}

	private void makeRemoteDirectory() {
		String cmd = new StringBuilder("mkdir -p ").append(
				trimRemoteDirectory(targetDir)).toString();
		resource.write(stringToState(cmd));
	}

	private void copyDirectory() {
		String cmd = new StringBuilder("scp -rpv ").append(sourceDir)
				.append(' ').append(targetDir).toString();
		resource.write(stringToState(cmd));
	}

	private ResourceState<String> rollbackOnCopying(String altered) {
		String cmd = new StringBuilder("rm -rf ")
				.append(trimRemoteDirectory(targetDir)).append("; mv ")
				.append(trimRemoteDirectory(altered)).append(' ')
				.append(trimRemoteDirectory(targetDir)).toString();
		return stringToState(cmd);
	}

	private ResourceState<String> deleteAfterCommit(String altered) {
		String cmd = new StringBuilder("rm -rf ").append(
				trimRemoteDirectory(altered)).toString();
		return stringToState(cmd);
	}

	private ResourceState<String> stringToState(String s) {
		return new NormalState<String>(s);
	}

	private String trimRemoteDirectory(String s) {
		int i = s.indexOf(':');
		if (i >= 0 && s.length() > i + 1) {
			return s.substring(i + 1);
		}
		return s;
	}
}
