package com.github.undo4j;


/**
 * WrappedClientRunnable
 * 
 * @author afs
 * @version 2013
 */

final class WrappedClientRunnable implements ClientCallable<Void> {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    private final ClientRunnable runnable;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class WrappedClientRunnable.
     */
    WrappedClientRunnable(final ClientRunnable client) {
        assert client != null;
        runnable = client;
    }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    @Override
    public Void call(final OperationDispatcher op) throws ClientException {
        runnable.run(op);
        return null;
    }
}
