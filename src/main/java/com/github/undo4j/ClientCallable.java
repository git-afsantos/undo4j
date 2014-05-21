package com.github.undo4j;


/**
 * ClientCallable is a Callable-like executable to be executed in a
 * transactional context.
 * 
 * @author afs
 * @version 2013
 */

public interface ClientCallable<T> {
    T call(OperationDispatcher dispatcher) throws ClientException;
}
