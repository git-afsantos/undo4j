package com.github.undo4j;


/**
 * TransactionAdapter
 * 
 * @author afs
 * @version 2013
 */

public abstract class TransactionAdapter implements TransactionListener {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class TransactionAdapter.
     */
    protected TransactionAdapter() {}



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    @Override
    public void started() {}

    /** */
    @Override
    public void committed() {}

    /** */
    @Override
    public void rolledBack() {}
}
