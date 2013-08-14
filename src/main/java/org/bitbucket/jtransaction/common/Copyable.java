package org.bitbucket.jtransaction.common;


/**
 * Copyable
 * 
 * @author afs
 * @version 2013
*/

public interface Copyable<T extends Copyable<T>> extends Cloneable {
    T clone();
}
