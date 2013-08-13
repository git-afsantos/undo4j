package org.bitbucket.jtransaction.functional;


/**
 * Function
 * 
 * @author afs
 * @version 2013
*/

public interface Function<A, B> {
    B call(A argument) throws Exception;
}
