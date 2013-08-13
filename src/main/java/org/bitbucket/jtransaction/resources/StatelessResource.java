package org.bitbucket.jtransaction.resources;

import org.bitbucket.jtransaction.common.LockManager;

/**
 * StatelessResource
 * 
 * @author afs
 * @version 2013
*/

public class StatelessResource<T> extends AbstractResource<T> {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class StatelessResource. */
    public StatelessResource(
		InternalResource<T> resource, LockManager lockManager
	) {
        super(resource, lockManager);
    }


    /** Copy constructor of objects of class StatelessResource. */
    protected StatelessResource(StatelessResource<T> instance) {
        super(instance);
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public final ResourceState<T> read() {
        try { return getInternalResource().buildState(); }
        catch (Exception e) {
            throw new ResourceReadException(e.getMessage(), e);
        }
    }

    /** */
    @Override
    public final void write(ResourceState<T> state) {
        try { getInternalResource().applyState(state); }
        catch (Exception e) {
            throw new ResourceWriteException(e.getMessage(), e);
        }
    }


    /** Overridable */
	@Override
	public void commit() {}


	/** Overridable */
	@Override
	public void rollback() {}


	/** Overridable */
	@Override
	public void update() {}


	/** */
	@Override
	public StatelessResource<T> clone() {
		return new StatelessResource<T>(this);
	}
}
