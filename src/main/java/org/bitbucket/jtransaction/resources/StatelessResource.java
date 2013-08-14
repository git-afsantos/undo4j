package org.bitbucket.jtransaction.resources;


/**
 * StatelessResource
 * 
 * @author afs
 * @version 2013
*/

public abstract class StatelessResource<T> extends AbstractResource<T> {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class StatelessResource. */
    public StatelessResource(InternalResource<T> resource) {
        super(resource);
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
}
