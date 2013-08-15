package org.bitbucket.jtransaction.sshrepo;

import java.util.LinkedList;
import java.util.List;

import org.bitbucket.jtransaction.resources.NormalState;
import org.bitbucket.jtransaction.resources.ResourceState;
import org.bitbucket.jtransaction.transactions.ManagedResource;
import org.bitbucket.jtransaction.transactions.TransactionalCallable;

public class ScpTransaction implements TransactionalCallable<Object> {
	private ManagedResource<String> resource;
	private String sourceDir, targetDir;
	
	public ScpTransaction(ScpResource r, String s, String t) {
		resource = ManagedResource.from(r);
		sourceDir = s;
		targetDir = t;
	}
	

	@Override
	public Object call() throws Exception {
		String stampedName = new StringBuilder().
				append(targetDir).
				append(System.currentTimeMillis()).toString();
		
		renameOldDirectory(stampedName);
		
		copyDirectory();

		return null;
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> list = new LinkedList<>();
		list.add(resource);
		return list;
	}
	
	
	private void renameOldDirectory(String name) {
		String cmd = new StringBuilder("mv ").
				append(targetDir).append(' ').
				append(name).toString();
		resource.write(stringToState(cmd));
	}
	
	
	private void copyDirectory() {
		String cmd = new StringBuilder("scp ").
				append(sourceDir).append(' ').
				append(targetDir).toString();
		resource.write(stringToState(cmd));
	}

	
	private ResourceState<String> stringToState(String s) {
		return new NormalState<String>(s);
	}
}
