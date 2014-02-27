package com.github.undo4j;

import java.util.concurrent.locks.Condition;


/**
 * InstantStrategy
 * 
 * @author afs
 * @version 2013
 */

final class InstantStrategy extends WaitStrategy {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class InstantStrategy.
     */
    InstantStrategy() { super(); }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    protected WaitMethod getWaitMethod() { return WaitMethod.INSTANT; }


    /**
     * Returns false. An instant strategy fails anytime it has to wait.
     */
    @Override
    protected boolean waitOn(final Condition condition) { return false; }
}
