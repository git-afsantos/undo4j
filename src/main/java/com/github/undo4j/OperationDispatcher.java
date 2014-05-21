package com.github.undo4j;


/**
 * OperationDispatcher
 * 
 * @author afs
 * @version 2013
 */

public interface OperationDispatcher {
    <T> T read(ReadOperation<T> op);
    void write(WriteOperation op);
    void abort();
    void abort(String message);
}
