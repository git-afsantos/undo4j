package com.github.undo4j.transactions;

import static com.github.undo4j.common.Check.checkArgument;

import com.github.undo4j.common.Copyable;
import com.github.undo4j.resources.Resource;
import com.github.undo4j.resources.ResourceId;
import com.github.undo4j.resources.ResourceState;

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
	private final ResourceListener listener;

	/**************************************************************************
	 * Constructors
	 **************************************************************************/

	/** Parameter constructor */
	ManagedResource(Resource<T> r, ResourceListener rl) {
		resource = r;
		controllers = new ThreadLocal<ResourceController>();
		listener = rl;
	}

	/** Copy constructor */
	private ManagedResource(ManagedResource<T> instance) {
		resource = instance.getResource();
		controllers = new ThreadLocal<ResourceController>();
		listener = instance.getListener();
	}

	/**************************************************************************
	 * Getters
	 **************************************************************************/

	/** Returns a copy, if possible. */
	Resource<T> getResource() {
		return resource;
	}

	/** */
	public ResourceId getId() {
		return resource.getId();
	}

	/** */
	public ResourceListener getListener() {
		return listener;
	}

	/**************************************************************************
	 * Predicates
	 **************************************************************************/

	/**************************************************************************
	 * Public Methods
	 **************************************************************************/

	/** */
	public ResourceState<T> read() {
		checkHasController();
		listener.readCalled(resource);
		ResourceState<T> s = controllers.get().read(resource);
		listener.readPerformed(resource);
		return s;
	}

	/** */
	public void write(ResourceState<T> state) {
		checkHasController();
		listener.writeCalled(resource);
		controllers.get().write(resource, state);
		listener.writePerformed(resource);
	}

	/** */
	public static <T> ManagedResource<T> from(Resource<T> resource) {
		return ManagedResource.from(resource, new NullResourceListener());
	}

	/** */
	public static <T> ManagedResource<T> from(Resource<T> resource, ResourceListener listener) {
		checkArgument(resource);
		checkArgument(listener);
		return new ManagedResource<T>(resource, listener);
	}

	/**************************************************************************
	 * Package Private Methods
	 **************************************************************************/

	/** */
	void commit() {
		listener.commitCalled(resource);
		controllers.get().commit(resource);
		listener.commitPerformed(resource);
	}

	/** */
	void rollback() {
		listener.rollbackCalled(resource);
		controllers.get().rollback(resource);
		listener.rollbackPerformed(resource);
	}

	/** */
	void update() {
		listener.updateCalled(resource);
		controllers.get().update(resource);
		listener.updatePerformed(resource);
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
		if (this == o) {
			return true;
		}
		if (!(o instanceof ManagedResource)) {
			return false;
		}
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
