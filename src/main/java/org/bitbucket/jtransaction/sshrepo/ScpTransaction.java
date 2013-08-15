package org.bitbucket.jtransaction.sshrepo;

import org.bitbucket.jtransaction.transactions.ManagedResource;
import org.bitbucket.jtransaction.transactions.TransactionalCallable;

public class ScpTransaction implements TransactionalCallable<Object> {

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		
		/*
		 * Save timestamp as string
		 * Rename existing folder on target server, using timestamp
		 * Copy directory
		 */
		
		
		return null;
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		// TODO Auto-generated method stub
		return null;
	}

}
