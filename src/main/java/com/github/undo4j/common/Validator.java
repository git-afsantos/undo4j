package com.github.undo4j.common;


/**
 * Validator
 * 
 * @author afs
 * @version 2013
*/

public interface Validator<T> {
    boolean isValid(T instance);
}
