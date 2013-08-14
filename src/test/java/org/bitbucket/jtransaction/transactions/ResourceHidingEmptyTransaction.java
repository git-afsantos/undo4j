package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.resources.ImmutableState;

public class ResourceHidingEmptyTransaction implements TransactionalCallable<String> {
	private final String text;
	private ManagedResource<String> resource;


	/** */
	public ResourceHidingEmptyTransaction(String s, ManagedResource<String> r) {
		text = s;
		resource = r;
	}

	@Override
	public String call() throws Exception {
		resource.write(new ImmutableState<String>(text));
		return text;
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		return null;
	}

}
