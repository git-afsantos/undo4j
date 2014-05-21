package com.github.undo4j;


/**
 * ClientRunnable is a Runnable-like executable to be executed
 * in a transactional context.
 * 
 * @author afs
 * @version 2013
 */

public interface ClientRunnable {
    void run(OperationDispatcher dispatcher) throws ClientException;
}
