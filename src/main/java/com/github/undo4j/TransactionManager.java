package com.github.undo4j;

import java.util.List;

/**
 * The transaction manager dispatches transactions.
 * The client doesn't implement this interface.
 * A factory provides the different transaction managers.
 * 
 * @author afs
 * @version 2013
 */

public interface TransactionManager {
    /** */
    <V> ResourceReference<V> register(V resource);

    /** */
    <V> List<ResourceReference<V>> registerAll(Iterable<V> resources);

    /** */
    void unregister(ResourceId resource);

    /** */
    void unregisterAll(Iterable<ResourceId> resources);

    /**
     * Executes the given runnable.
     * Returns when the task is complete.
     * 
     * @throws TransactionExecutionException
     *  - if the execution fails.
     */
    void execute(ClientRunnable runnable)
        throws TransactionExecutionException;

    /**
     * Executes the given runnable, with the given configurations.
     * Returns when the task is complete.
     * 
     * @throws TransactionExecutionException
     *  - if the execution fails.
     */
    void execute(ClientRunnable runnable,
        TransactionConfigurations config)
            throws TransactionExecutionException;


    /**
     * Executes the given callable.
     * Returns its result when the task is complete.
     * 
     * @throws TransactionExecutionException
     *  - if the execution fails.
     */
    <T> T execute(ClientCallable<T> callable)
        throws TransactionExecutionException;

    /**
     * Executes the given callable, with the given configurations.
     * Returns its result when the task is complete.
     * 
     * @throws TransactionExecutionException
     *  - if the execution fails.
     */
    <T> T execute(ClientCallable<T> callable,
        TransactionConfigurations config)
            throws TransactionExecutionException;


    /** */
    boolean isShutdown();

    /** */
    void shutdown();
}
