/*
The MIT License (MIT)

Copyright (c) 2013 Andre Santos, Victor Miraldo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/


package org.bitbucket.afsantos.jtransaction.transactions;

import org.bitbucket.afsantos.jtransaction.common.AccessMode;
import org.bitbucket.afsantos.jtransaction.common.IsolationLevel;

import static org.bitbucket.afsantos.jtransaction.common.Check.checkArgument;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * AbstractTransaction
 * 
 * @author afs
 * @version 2013
*/

abstract class AbstractTransaction<T> implements Transaction<T> {
    // instance variables
    private Thread thread = Thread.currentThread();
    private final TransactionId id = TransactionId.newTransactionId();
    private final AccessMode mode;
    private final IsolationLevel isolation;
    private final TransactionListener listener;
    private final TransactionStatistics stats = new TransactionStatistics();
    private final Map<String, ResourceController> controllers =
            new HashMap<>();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class AbstractTransaction. */
    protected AbstractTransaction(
            AccessMode accessMode,
            IsolationLevel isolationLevel,
            TransactionListener transactionListener
    ) {
        checkArgument("null access mode", accessMode);
        checkArgument("null isolation level", isolationLevel);
        checkArgument("null listener", transactionListener);
        this.mode = accessMode;
        this.isolation = isolationLevel;
        this.listener = transactionListener;
    }


    /** Copy constructor of objects of class AbstractTransaction. */
    protected AbstractTransaction(AbstractTransaction<T> instance) {
        this(
            instance.getAccessMode(),
            instance.getIsolationLevel(),
            instance.getListener()
        );
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    @Override
    public final TransactionId getId() { return this.id; }

    /** */
    @Override
    public final AccessMode getAccessMode() { return this.mode; }

    /** */
    @Override
    public final IsolationLevel getIsolationLevel() { return this.isolation; }

    /** */
    protected final TransactionStatistics getStatistics() {
        return this.stats.clone();
    }

    /** */
    protected final TransactionListener getListener() {
        return this.listener;
    }

    /** */
    protected final Map<String, ResourceController> getControllers() {
        return this.controllers;
    }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    protected final void setThread() { this.thread = Thread.currentThread(); }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    protected final boolean isEmpty() {
        return !this.stats.hasUsedResources();
    }

    /** */
    @Override
    public final boolean isReader() {
        return this.mode == AccessMode.READ;
    }

    /** */
    @Override
    public final boolean isWriter() {
        return this.mode == AccessMode.WRITE;
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public final void readPerformed(String resource) {
        this.stats.putIfAbsent(resource);
        this.stats.incrementReadCount(resource);
    }

    /** */
    @Override
    public final void writePerformed(String resource) {
        this.stats.putIfAbsent(resource);
        this.stats.incrementWriteCount(resource);
    }


    /** */
    @Override
    public final void putController(String res, ResourceController ctrl) {
        this.controllers.put(res, ctrl);
        this.stats.incrementRequestedCount();
    }


    /** */
    @Override
    public final void interrupt() { this.thread.interrupt(); }


    /** */
    protected final void commit() {
        // Attempt commit on each resource.
        // Execution should abort as soon as an error is encountered.
        Collection<ResourceController> ctrls = this.controllers.values();
        // Try ------------------------------------------------------
        for (ResourceController c : ctrls) { c.commit(); }
        // If all commits executed successfully, update.
        for (ResourceController c : ctrls) { c.update(); }
        // End Try --------------------------------------------------
        // Notify listener.
        this.listener.terminate();
    }

    /** */
    protected final void rollback() {
        // Attempt to rollback on each resource.
        // Execution is aborted as soon as an error is encountered.
        Collection<ResourceController> ctrls = this.controllers.values();
        // Try ------------------------------------------------------
        for (ResourceController c : ctrls) { c.rollback(); }
        // End Try --------------------------------------------------
        // Notify listener.
        this.listener.terminate();
    }

    /** Releases all handles kept. */
    protected final void releaseHandles() {
        Collection<ResourceController> ctrls = this.controllers.values();
        for (ResourceController c : ctrls) { c.release(); }
        this.controllers.clear();
    }


    /** */
    protected final void rollbackAndRelease() {
        try { rollback(); }
        finally { releaseHandles(); }
    }


    /** */
    protected final void checkNotEmpty() {
        if (isEmpty()) {
            throw new TransactionEmptyException("empty transaction");
        }
    }



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Equivalence relation. Contract (for any non-null reference values
            x, y, and z):
        * Reflexive: x.equals(x).
        * Symmetric: x.equals(y) iff y.equals(x).
        * Transitive: if x.equals(y) and y.equals(z), then x.equals(z).
        * Consistency: successive calls (with no modification of the equality
            fields) return the same result.
        * x.equals(null) should return false.
    */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        AbstractTransaction<?> n = (AbstractTransaction<?>) o;
        return this.id.equals(n.getId());
    }

    /** Contract:
        * Consistency: successive calls (with no modification of the equality
            fields) return the same code.
        * Function: two equal objects have the same (unique) hash code.
        * (Optional) Injection: unequal objects have different hash codes.
    * Common practices:
        * boolean: calculate (f ? 0 : 1);
        * byte, char, short or int: calculate (int) f;
        * long: calculate (int) (f ^ (f >>> 32));
        * float: calculate Float.floatToIntBits(f);
        * double: calculate Double.doubleToLongBits(f)
            and handle the return value like every long value;
        * Object: use (f == null ? 0 : f.hashCode());
        * Array: recursion and combine the values.
    * Formula:
        hash = prime * hash + codeForField
    */
    @Override
    public final int hashCode() {
        return this.id.hashCode();
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public abstract AbstractTransaction<T> clone();
}
