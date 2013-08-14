package org.bitbucket.jtransaction.transactions;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.jtransaction.resources.ImmutableState;

public class WriteTransaction implements TransactionalCallable<String> {
	private final String text;
	private ManagedResource<String> resource;


	/** */
	public WriteTransaction(String s, ManagedResource<String> r) {
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
		List<ManagedResource<?>> list = new ArrayList<>();
		list.add(resource);
		return list;
	}

}
