package com.github.undo4j;

import java.util.concurrent.Callable;


/**
 * Transaction
 * 
 * @author afs
 * @version 2013
 */

final class Transaction<T> implements Callable<T> {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** */
    private final TransactionId id;

    /** */
    private final ClientCallable<? extends T> client;

    /** */
    private final DefaultDispatcher dispatcher;

    /** */
    private final TransactionRegistry registry;

    /** */
    private final TransactionListener[] listeners;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class Transaction.
     */
    protected Transaction(
            final TransactionId id,
            final ClientCallable<? extends T> client,
            final DefaultDispatcher dispatcher,
            final TransactionRegistry registry,
            final TransactionListener[] listeners) {
        assert id != null && client != null && dispatcher != null
            && registry != null && listeners != null;
        this.id         = id;
        this.client     = client;
        this.dispatcher = dispatcher;
        this.registry   = registry;
        this.listeners  = listeners;
    }



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    TransactionId getId() { return id; }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    @Override
    public T call() throws TransactionExecutionException {
        TransactionExecutionException.Builder builder =
            TransactionExecutionException.newBuilder();
        // Setup --------------------------------------------------------------
        notifyStart(builder);                       // Possible exception
        abortIfException(builder);
        bind();
        // Client code --------------------------------------------------------
        T result        = null;
        boolean success = false;
        try {
            result = client.call(dispatcher);       // Possible exception
            dispatcher.commit();                    // Possible exception
            success = true;
        } catch (ClientException | RuntimeException ex) {
            // Cleanup - Try to roll back -------------------------------------
            builder.onExecution(ex);
            builder.setMessage("Exception during execution");
            rollback(builder);                      // Possible exception
        } finally {
            // Cleanup - Release resources ------------------------------------
            unbind();
            notifyTermination(success, builder);    // Possible exception
            abortIfException(builder);
        }
        return result;
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */
    void interrupt() {
        dispatcher.interrupt();
    }


    /**
     * Transactions must bind themselves in the registry.
     * This allows the manager to prevent a transaction
     * from spawning another transaction.
     */
    void bind() {
        dispatcher.bind();
        registry.bind(id);
    }


    /** */
    void unbind() {
        dispatcher.shutdown();
        dispatcher.release();
        dispatcher.unbind();
        registry.unbind();
    }


    /** */
    private void rollback
            (final TransactionExecutionException.Builder builder) {
        try {
            dispatcher.rollback();
        } catch (RuntimeException ex) {
            builder.onRollback(ex);
            builder.setMessage("Exception on rollback");
        }
    }


    /** */
    private void notifyStart
            (final TransactionExecutionException.Builder builder) {
        try {
            for (TransactionListener tl: listeners) {
                tl.started();
            }
        } catch (Exception ex) {
            builder.onStart(ex);
            builder.setMessage("Exception on start-up");
        }
    }


    /** */
    private void notifyTermination(final boolean success,
            final TransactionExecutionException.Builder builder) {
        try {
            for (TransactionListener tl: listeners) {
                if (success) { tl.committed(); }
                else { tl.rolledBack(); }
            }
        } catch (RuntimeException ex) {
            builder.onTermination(ex);
            builder.setMessage("Exception on terminaton");
        }
    }


    /** */
    private void abortIfException
        (final TransactionExecutionException.Builder builder)
            throws TransactionExecutionException {
        if (builder.hasExceptions()) {
            throw builder.build();
        }
    }



    /*************************************************************************\
     *  Equals, HashCode, ToString & Clone
    \*************************************************************************/

    /**
     *  Equivalence relation.
     *  Contract (for any non-null reference values x, y, and z):
     *      Reflexive: x.equals(x).
     *      Symmetric: x.equals(y) iff y.equals(x).
     *      Transitive: if x.equals(y) and y.equals(z), then x.equals(z).
     *      Consistency: successive calls return the same result,
     *          assuming no modification of the equality fields.
     *      x.equals(null) should return false.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Transaction)) { return false; }
        Transaction<?> n = (Transaction<?>) o;
        return id.equals(n.id);
    }

    /**
     *  Contract:
     *      Consistency: successive calls return the same code,
     *          assuming no modification of the equality fields.
     *      Function: two equal objects have the same (unique) hash code.
     *      (Optional) Injection: unequal objects have different hash codes.
     *
     *  Common practices:
     *      boolean: calculate (f ? 0 : 1);
     *      byte, char, short or int: calculate (int) f;
     *      long: calculate (int) (f ^ (f >>> 32));
     *      float: calculate Float.floatToIntBits(f);
     *      double: calculate Double.doubleToLongBits(f)
     *          and handle the return value like every long value;
     *      Object: use (f == null ? 0 : f.hashCode());
     *      Array: recursion and combine the values.
     *
     *  Formula:
     *      hash = prime * hash + codeForField
     */
    @Override
    public int hashCode() { return id.hashCode(); }

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        return sb.toString();
    }
}
