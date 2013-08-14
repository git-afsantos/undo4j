package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.resources.ImmutableState;
import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceState;

public final class StringResource implements InternalResource<String> {
	// instance variables
	private String myString = "";

	@Override
	public boolean isValidState(ResourceState<String> state) {
		return !state.isNull();
	}

	@Override
	public ResourceState<String> buildState() {
		return new ImmutableState<String>(myString);
	}

	@Override
	public void applyState(ResourceState<String> state) {
		myString = state.get();
	}

}
