package org.bitbucket.jtransaction.resources;


/**
 * PassiveInternalResource
 * 
 * @author afs
 * @version 2013
*/

public final class PassiveInternalResource implements InternalResource {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class PassiveInternalResource. */
    public PassiveInternalResource() {}

    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    public boolean isValidState(ResourceState state) {
        return true;
    }

    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    public void initialize() {}

    /** */
    public void dispose() {}

    /** */
    public ResourceState buildState() {
        return new NullState();
    }

    /** */
    public void applyState(ResourceState state) {}
}
