package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.Copyable;
import org.bitbucket.jtransaction.resources.Resource;
import org.bitbucket.jtransaction.resources.ResourceId;
import org.bitbucket.jtransaction.resources.ResourceState;

/**
 * ManagedResource
 * 
 * @author afs
 * @version 2013
*/


public final class ManagedResource<T> implements Copyable<ManagedResource<T>> {
	// instance variables
	private final Resource<T> resource;
	private final ThreadLocal<ResourceController> controllers;


	/**************************************************************************
     * Constructors
    **************************************************************************/

	/** Parameter constructor */
	ManagedResource(Resource<T> r) {
		resource = r;
		controllers = new ThreadLocal<ResourceController>();
	}


	/** Copy constructor */
	private ManagedResource(ManagedResource<T> instance) {
		resource = instance.getResource();
		controllers = new ThreadLocal<ResourceController>();
	}


	/**************************************************************************
     * Getters
    **************************************************************************/

	/** Returns a copy, if possible. */
	Resource<T> getResource() { return resource; }


	/** */
	public ResourceId getId() { return resource.getId(); }


	/**************************************************************************
     * Predicates
    **************************************************************************/


	/**************************************************************************
     * Public Methods
    **************************************************************************/

	/** */
	public ResourceState<T> read() {
		checkHasController();
		return controllers.get().read(resource);
	}


	/** */
	public void write(ResourceState<T> state) {
		checkHasController();
		controllers.get().write(resource, state);;
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
		controllers.get().commit(resource);
	}


	/** */
	void rollback() {
		controllers.get().rollback(resource);
	}


	/** */
	void update() {
		controllers.get().update(resource);
	}


	/** */
	void release() {
		controllers.get().release(resource);
	}


	/** */
	void putController(ResourceController rc) {
		controllers.set(rc);
	}


	/** */
	void removeController() {
		controllers.remove();
	}


	/**************************************************************************
     * Private Methods
    **************************************************************************/

	/** */
	private void checkHasController() {
		if (controllers.get() == null) {
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
