package org.bitbucket.jtransaction.common;


/**
 * NullLockManager
 * 
 * @author afs
 * @version 2013
*/

final class NullLockManager extends LockManager {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class NullLockManager. */
    NullLockManager() { super(IsolationLevel.NONE); }


    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public boolean acquire(AccessMode mode) { return true; }

    /** */
    @Override
    public void release() {}


	@Override
	public LockManager clone() {
		return new NullLockManager();
	}
}
