package com.github.undo4j;

import java.io.PrintStream;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Exception thrown when attempting to retrieve the result of a
 * transaction that aborted by throwing an exception.
 * 
 * @author afs
 * @version 2013
 */

public final class TransactionExecutionException extends ExecutionException {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static final long serialVersionUID = 98723498725409234L;



    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** */
    private final Exception onStart;

    /** */
    private final Exception onExecution;

    /** */
    private final Exception onRollback;

    /** */
    private final Exception onTermination;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of class TransactionExecutionException.
     */
    private TransactionExecutionException(
            final String message,
            final Exception onStart,
            final Exception onExecution,
            final Exception onRollback,
            final Exception onTermination) {
        super(message,
            onStart         != null ? onStart
            : onExecution   != null ? onExecution
            : onRollback    != null ? onRollback
            : onTermination);
        this.onStart        = onStart;
        this.onExecution    = onExecution;
        this.onRollback     = onRollback;
        this.onTermination  = onTermination;
    }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public Throwable getExceptionOnStart() {
        return onStart;
    }

    /** */
    public Throwable getExceptionOnExecution() {
        return onExecution;
    }

    /** */
    public Throwable getExceptionOnRollback() {
        return onRollback;
    }

    /** */
    public Throwable getExceptionOnTermination() {
        return onTermination;
    }


    /** */
    public List<Throwable> getAllExceptions() {
        List<Throwable> list = new ArrayList<>();
        if (onStart != null)        { list.add(onStart);        }
        if (onExecution != null)    { list.add(onExecution);    }
        if (onRollback != null)     { list.add(onRollback);     }
        if (onTermination != null)  { list.add(onTermination);  }
        assert !list.isEmpty();
        return list;
    }


    /** */
    @Override
    public void printStackTrace() {
        if (onStart != null)        { onStart.printStackTrace();        }
        if (onExecution != null)    { onExecution.printStackTrace();    }
        if (onRollback != null)     { onRollback.printStackTrace();     }
        if (onTermination != null)  { onTermination.printStackTrace();  }
    }


    /** */
    @Override
    public void printStackTrace(final PrintStream ps) {
        if (onStart != null)        { onStart.printStackTrace(ps);          }
        if (onExecution != null)    { onExecution.printStackTrace(ps);      }
        if (onRollback != null)     { onRollback.printStackTrace(ps);       }
        if (onTermination != null)  { onTermination.printStackTrace(ps);    }
    }


    /** */
    @Override
    public void printStackTrace(final PrintWriter pw) {
        if (onStart != null)        { onStart.printStackTrace(pw);          }
        if (onExecution != null)    { onExecution.printStackTrace(pw);      }
        if (onRollback != null)     { onRollback.printStackTrace(pw);       }
        if (onTermination != null)  { onTermination.printStackTrace(pw);    }
    }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    static Builder newBuilder() {
        return new Builder();
    }



    /*************************************************************************\
     *  Builder
    \*************************************************************************/

    /** */
    static final class Builder {
        private String      message             = null;
        private Exception   onNotifyStart       = null;
        private Exception   onExecution         = null;
        private Exception   onRollback          = null;
        private Exception   onNotifyTermination = null;


        /** */
        boolean hasExceptions() {
            return onNotifyStart        != null
                || onExecution          != null
                || onRollback           != null
                || onNotifyTermination  != null;
        }


        /** */
        TransactionExecutionException build() {
            if (!hasExceptions()) {
                throw new AssertionError();
            }
            return new TransactionExecutionException
                (message, onNotifyStart, onExecution,
                onRollback, onNotifyTermination);
        }


        /** */
        Builder onStart(Exception ex) {
            onNotifyStart = ex;
            return this;
        }

        /** */
        Builder onExecution(Exception ex) {
            onExecution = ex;
            return this;
        }

        /** */
        Builder onRollback(Exception ex) {
            onRollback = ex;
            return this;
        }

        /** */
        Builder onTermination(Exception ex) {
            onNotifyTermination = ex;
            return this;
        }

        /** */
        Builder setMessage(String msg) {
            message = msg;
            return this;
        }
    }
}
