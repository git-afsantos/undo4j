package com.github.undo4j;

import java.util.List;
import java.util.concurrent.Future;

/**
 * The transaction manager dispatches transactions.
 * The client doesn't implement this interface.
 * A factory provides the different transaction managers.
 * The manager allows the client to register shared resources.
 * The client may also choose the desired isolation level.
 * 
 * @author afs
 * @version 2013
 */

public interface ConcurrentTransactionManager extends TransactionManager {
    /** */
    <V> ResourceReference<V> registerShareable(V resource);

    /** */
    <V> List<ResourceReference<V>> registerAllShareable(Iterable<V> resources);


    /**
     * Submits the given runnable for execution, when possible.
     * The task is complete when the Future's get returns.
     * 
     * The Future's ExecutionException contains a
     * TransactionExecutionException as its cause.
     */
    Future<Void> submit(ClientRunnable runnable);

    /**
     * Submits the given runnable for execution, when possible,
     * with the given configurations.
     * The task is complete when the Future's get returns.
     * 
     * The Future's ExecutionException contains a
     * TransactionExecutionException as its cause.
     */
    Future<Void> submit(ClientRunnable runnable,
        TransactionConfigurations config);

    /**
     * Submits the given callable for execution, when possible.
     * The callable's result is obtained through the Future's get.
     * 
     * The Future's ExecutionException contains a
     * TransactionExecutionException as its cause.
     */
    <T> Future<T> submit(ClientCallable<T> tc);

    /**
     * Submits the given callable for execution, when possible,
     * with the given configurations.
     * The callable's result is obtained through the Future's get.
     * 
     * The Future's ExecutionException contains a
     * TransactionExecutionException as its cause.
     */
    <T> Future<T> submit(ClientCallable<T> tc,
        TransactionConfigurations config);
}
