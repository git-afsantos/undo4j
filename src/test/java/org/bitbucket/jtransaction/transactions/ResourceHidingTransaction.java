package org.bitbucket.jtransaction.transactions;

import java.util.ArrayList;
import java.util.List;

import org.bitbucket.jtransaction.resources.ImmutableState;

public class ResourceHidingTransaction implements TransactionalCallable<String> {
	private final String text;
	private ManagedResource<String> resource, hidden;


	/** */
	public ResourceHidingTransaction(
		String s, ManagedResource<String> r, ManagedResource<String> h
	) {
		text = s;
		resource = r;
		hidden = h;
	}

	@Override
	public String call() throws Exception {
		resource.write(new ImmutableState<String>(text));
		hidden.write(new ImmutableState<String>(text));
		return text;
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> l = new ArrayList<>();
		l.add(resource);
		return l;
	}
}
