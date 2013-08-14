package org.bitbucket.jtransaction.resources;


/**
 * InternalResource is the object to be passed to Resources.
 * These manage the access to InternalResources.
 * 
 * @author afs
 * @version 2013
*/

public interface InternalResource<T> {
    /** Performs any necessary resource initialization.
     * This method is allowed to throw any exception.
    */
    void initialize() throws Exception;


    /** Performs any necessary resource clean up.
     * This method is allowed to throw any exception.
    */
    void dispose() throws Exception;


    /** Tests whether the given state is valid.
     */
    boolean isValidState(ResourceState<T> state);


    /** Builds and returns a representation of this resource's state.
     * This method is allowed to throw any exception,
     * if a state can't be built.
    */
    ResourceState<T> buildState() throws Exception;


    /** Applies the given state to this resource.
     * This method is allowed to throw any exception,
     * if the changes can't be applied.
    */
    void applyState(ResourceState<T> state) throws Exception;
}
