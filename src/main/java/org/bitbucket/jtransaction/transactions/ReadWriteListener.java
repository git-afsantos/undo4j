package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.resources.Resource;

/**
 * ReadWriteListener
 * 
 * @author afs
 * @version 2013
*/

interface ReadWriteListener {
	<T> void readCalled(Resource<T> resource);
	<T> void writeCalled(Resource<T> resource);
    <T> void readPerformed(Resource<T> resource);
    <T> void writePerformed(Resource<T> resource);
}
