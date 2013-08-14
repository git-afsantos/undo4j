package org.bitbucket.jtransaction.resources;

import org.bitbucket.jtransaction.common.Acquirable;
import org.bitbucket.jtransaction.common.Disposable;
import org.bitbucket.jtransaction.common.Initializable;
import org.bitbucket.jtransaction.common.VersionedObject;


/**
 * Resource
 * 
 * @author afs
 * @version 2013
*/

public interface Resource<T> extends Acquirable,
        VersionedObject<ResourceState<T>>, Initializable, Disposable {
    /** Checks whether this resource can be accessed, in its current state.
     */
    boolean isAccessible();


    /** Sets whether this resource can be accessed.
     */
    void setAccessible(boolean accessible);


    /** Checks whether this resource is in a consistent state.
     * The level of consistency guaranteed is, at least, that commit or rollback
     * operations completed successfully.
     */
    boolean isConsistent();


    /** Sets whether this resource is in a consistent state.
     */
    void setConsistent(boolean isConsistent);
}
