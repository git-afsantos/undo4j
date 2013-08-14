package org.bitbucket.jtransaction.common;


/**
 * Validator
 * 
 * @author afs
 * @version 2013
*/

public interface Validator<T> {
    boolean isValid(T instance);
}
