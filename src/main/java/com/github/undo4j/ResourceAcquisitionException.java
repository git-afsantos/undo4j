package com.github.undo4j;


/**
 * ResourceAcquisitionException
 * 
 * @author afs
 * @version 2013
 */

public final class ResourceAcquisitionException
        extends TransactionRuntimeException {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 1L;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of class ResourceAcquisitionException.
     */
    ResourceAcquisitionException() { super(); }


    /**
     *  Parameter constructor of class ResourceAcquisitionException.
     */
    ResourceAcquisitionException(final String message) { super(message); }


    /**
     *  Parameter constructor of class ResourceAcquisitionException.
     */
    ResourceAcquisitionException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }

    /**
     *  Parameter constructor of class ResourceAcquisitionException.
     */
    ResourceAcquisitionException(final Throwable cause) { super(cause); }
}
