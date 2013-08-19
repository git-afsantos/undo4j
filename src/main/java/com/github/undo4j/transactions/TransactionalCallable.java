package com.github.undo4j.transactions;

import java.util.concurrent.Callable;

public interface TransactionalCallable<V> extends Callable<V> {
	Iterable<ManagedResource<?>> getManagedResources();
}
