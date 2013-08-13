package org.bitbucket.jtransaction.resources;


/**
 * ReadWriteListener
 * 
 * @author afs
 * @version 2013
*/

public interface ReadWriteListener {
	/** To be called before the read happens. */
	<T> void readCalled(Resource<T> resource);

	/** To be called before the write happens. */
    <T> void writeCalled(Resource<T> resource);

    /** To be called after the read happens. */
	<T> void readPerformed(Resource<T> resource);

	/** To be called after the write happens. */
    <T> void writePerformed(Resource<T> resource);
}
