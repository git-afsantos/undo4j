package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.Copyable;
import org.bitbucket.jtransaction.resources.Resource;
import org.bitbucket.jtransaction.resources.ResourceState;

/**
 * ManagedResource
 * 
 * @author afs
 * @version 2013
*/

/**
 * 
 * TODO:
 * produced by factory
 * wraps a resource
 * offers read and write, just like the handles
 * commit etc are package-private
 * adding listeners, or producing handles is also package private
 *
 */


public final class ManagedResource<T> implements Copyable<ManagedResource<T>> {
	// instance variables
	private final Resource<T> resource;
	private final ThreadLocal<ResourceListener> listeners;


	/**************************************************************************
     * Constructors
    **************************************************************************/

	/** Parameter constructor */
	ManagedResource(Resource<T> r) {
		resource = r;
		listeners = new ThreadLocal<ResourceListener>();
	}


	/** Copy constructor */
	private ManagedResource(ManagedResource<T> instance) {
		resource = instance.getResource();
		listeners = new ThreadLocal<ResourceListener>();
	}


	/**************************************************************************
     * Getters
    **************************************************************************/

	/** Returns a copy, if possible. */
	Resource<T> getResource() { return resource; }


	/**************************************************************************
     * Predicates
    **************************************************************************/


	/**************************************************************************
     * Public Methods
    **************************************************************************/

	/** */
	public ResourceState<T> read() {
		checkHasListener();
		ResourceListener rl = listeners.get();
		rl.readCalled(resource);
		ResourceState<T> state = resource.read();
		rl.readPerformed(resource);
		return state;
	}


	/** */
	public void write(ResourceState<T> state) {
		checkHasListener();
		ResourceListener rl = listeners.get();
		rl.writeCalled(resource);
		resource.write(state);
		rl.writePerformed(resource);
	}



	/** */
	public static <T> ManagedResource<T> from(Resource<T> resource) {
		return new ManagedResource<T>(resource);
	}


	/**************************************************************************
     * Package Private Methods
    **************************************************************************/

	/** */
	void commit() {
		checkHasListener();
		ResourceListener rl = listeners.get();
		rl.commitCalled(resource);
		resource.commit();
		rl.commitPerformed(resource);
	}


	/** */
	void rollback() {
		checkHasListener();
		ResourceListener rl = listeners.get();
		rl.rollbackCalled(resource);
		resource.rollback();
		rl.rollbackPerformed(resource);
	}


	/** */
	void update() {
		checkHasListener();
		ResourceListener rl = listeners.get();
		rl.updateCalled(resource);
		resource.update();
		rl.updatePerformed(resource);
	}


	/**************************************************************************
     * Private Methods
    **************************************************************************/

	/** */
	private void checkHasListener() {
		if (listeners.get() == null) {
			throw new IllegalResourceStateException();
		}
	}


	/**************************************************************************
     * Equals, HashCode, ToString, Clone
    **************************************************************************/

	/** */
	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (!(o instanceof ManagedResource)) { return false; }
		ManagedResource<?> n = (ManagedResource<?>) o;
		return resource.equals(n.getResource());
	}


	/** */
	@Override
	public int hashCode() {
		return resource.hashCode();
	}


	/** */
	@Override
	public String toString() {
		return resource.toString();
	}


	/** */
	@Override
	public ManagedResource<T> clone() {
		return new ManagedResource<T>(this);
	}
}
