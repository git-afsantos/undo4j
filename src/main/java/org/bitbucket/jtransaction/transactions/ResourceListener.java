package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.resources.Resource;

interface ResourceListener extends ReadWriteListener {
	<T> void commitCalled(Resource<T> resource);
	<T> void rollbackCalled(Resource<T> resource);
	<T> void updateCalled(Resource<T> resource);
    <T> void commitPerformed(Resource<T> resource);
    <T> void rollbackPerformed(Resource<T> resource);
    <T> void updatePerformed(Resource<T> resource);
}
