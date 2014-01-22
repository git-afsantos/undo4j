package com.github.undo4j;

import static com.github.undo4j.Check.checkArgument;
import static com.github.undo4j.Check.checkNotNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TransactionConfigurations
 * 
 * @author afs
 * @version 2013
 */

public final class TransactionConfigurations {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    /** */
    public static final AccessMode DEFAULT_ACCESS_MODE = AccessMode.WRITE;

    /** */
    public static final AcquireStrategy DEFAULT_ACQUIRE_STRATEGY =
        AcquireStrategy.BLOCKING;

    /** */
    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    /** */
    public static final long DEFAULT_TIMEOUT_DELAY = 1L;


    /** */
    private static final CommitOperation NULL_COMMIT_OPERATION =
        new NullCommitOperation();



    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** */
    private final AccessMode mode;

    /** */
    private final AcquireStrategy strategy;

    /** */
    private final TimeUnit unit;

    /** */
    private final long timeout;

    /** */
    private final TransactionListener[] listeners;

    /** */
    private final CommitOperation commitOperation;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class TransactionConfigurations.
     */
    private TransactionConfigurations(
            final AccessMode accessMode,
            final AcquireStrategy acquireStrategy,
            final TimeUnit timeUnit,
            final long timeoutDelay,
            final TransactionListener[] transactionListeners,
            final CommitOperation onCommit) {
        mode = accessMode;
        strategy = acquireStrategy;
        unit = timeUnit;
        timeout = timeoutDelay;
        listeners = transactionListeners;
        commitOperation = onCommit;
    }



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    public AccessMode getAccessMode() { return mode; }

    /** */
    public AcquireStrategy getAcquireStrategy() { return strategy; }

    /** */
    public TimeUnit getTimeUnit() { return unit; }

    /** */
    public long getTimeoutDelay() { return timeout; }

    /** */
    public CommitOperation getCommitOperation() {
        return commitOperation;
    }

    /** */
    public TransactionListener[] getListeners() {
        TransactionListener[] array =
            new TransactionListener[listeners.length];
        System.arraycopy(listeners, 0, array, 0, listeners.length);
        return array;
    }



    /*************************************************************************\
     *  Predicates
    \*************************************************************************/

    /** */



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static TransactionConfigurations defaults() {
        return new TransactionConfigurations
            (DEFAULT_ACCESS_MODE,
            DEFAULT_ACQUIRE_STRATEGY,
            DEFAULT_TIME_UNIT,
            DEFAULT_TIMEOUT_DELAY,
            new TransactionListener[0],
            NULL_COMMIT_OPERATION);
    }


    /** */
    public static Builder newBuilder() {
        return new Builder
            (DEFAULT_ACCESS_MODE,
            DEFAULT_ACQUIRE_STRATEGY,
            DEFAULT_TIME_UNIT,
            DEFAULT_TIMEOUT_DELAY,
            new TransactionListener[0],
            NULL_COMMIT_OPERATION);
    }


    /** */
    public Builder toBuilder() {
        return new Builder(mode, strategy, unit,
            timeout, listeners, commitOperation);
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */



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
        if (!(o instanceof TransactionConfigurations)) { return false; }
        TransactionConfigurations n = (TransactionConfigurations) o;
        return  mode        ==  n.mode      &&
                strategy    ==  n.strategy  &&
                unit        ==  n.unit      &&
                timeout     ==  n.timeout;
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
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + mode.hashCode();
        hash = 31 * hash + strategy.hashCode();
        hash = 31 * hash + unit.hashCode();
        hash = 31 * hash + (int) (timeout ^ (timeout >>> 32));
        return hash;
    }

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(mode);
        return sb.toString();
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /** */
    public static final class Builder {

        /*********************************************************************\
         *  Attributes
        \*********************************************************************/

        private AccessMode mode;
        private AcquireStrategy strategy;
        private TimeUnit unit;
        private long timeout;
        private List<TransactionListener> listeners;
        private CommitOperation commitOperation;


        /*********************************************************************\
         *  Constructors
        \*********************************************************************/

        /** Parameter constructor */
        Builder(
                final AccessMode accessMode,
                final AcquireStrategy acquireStrategy,
                final TimeUnit timeUnit,
                final long timeoutDelay,
                final TransactionListener[] transactionListeners,
                final CommitOperation onCommit) {
            mode            = accessMode;
            strategy        = acquireStrategy;
            unit            = timeUnit;
            timeout         = timeoutDelay;
            commitOperation = onCommit;
            listeners       = new LinkedList<>
                (Arrays.asList(transactionListeners));
        }


        /*********************************************************************\
         *  Getters
        \*********************************************************************/

        /** */
        public AccessMode getAccessMode() { return mode; }

        /** */
        public AcquireStrategy getAcquireStrategy() { return strategy; }

        /** */
        public TimeUnit getTimeUnit() { return unit; }

        /** */
        public long getTimeoutDelay() { return timeout; }

        /** */
        public CommitOperation getCommitOperation() {
            return commitOperation;
        }

        /** */
        public TransactionListener[] getListeners() {
            return listeners
                .toArray(new TransactionListener[listeners.size()]);
        }


        /*********************************************************************\
         *  Setters
        \*********************************************************************/

        /** */
        public Builder setAccessMode(final AccessMode accessMode) {
            checkNotNull(accessMode);
            mode = accessMode;
            return this;
        }

        /** */
        public Builder setAcquireStrategy(
                final AcquireStrategy acquireStrategy) {
            checkNotNull(acquireStrategy);
            strategy = acquireStrategy;
            return this;
        }

        /** */
        public Builder setTimeUnit(final TimeUnit timeUnit) {
            checkNotNull(timeUnit);
            unit = timeUnit;
            return this;
        }

        /** */
        public Builder setTimeoutDelay(final long timeoutDelay) {
            checkArgument(timeoutDelay >= 0);
            timeout = timeoutDelay;
            return this;
        }

        /** */
        public Builder setCommitOperation(final CommitOperation op) {
            if (op == null) { commitOperation = NULL_COMMIT_OPERATION; }
            else { commitOperation = op; }
            return this;
        }

        /** */
        public Builder addListener(final TransactionListener listener) {
            checkNotNull(listener);
            listeners.add(listener);
            return this;
        }


        /*********************************************************************\
         *  Public Methods
        \*********************************************************************/

        /** */
        public TransactionConfigurations build() {
            return new TransactionConfigurations
                (mode, strategy, unit, timeout,
                this.getListeners(), commitOperation);
        }


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
            if (o == null || this.getClass() != o.getClass()) { return false; }
            Builder n = (Builder) o;
            return  mode        ==  n.mode      &&
                    strategy    ==  n.strategy  &&
                    unit        ==  n.unit      &&
                    timeout     ==  n.timeout;
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
        public int hashCode() {
            int hash = 17;
            hash = 31 * hash + mode.hashCode();
            hash = 31 * hash + strategy.hashCode();
            hash = 31 * hash + unit.hashCode();
            hash = 31 * hash + (int) (timeout ^ (timeout >>> 32));
            return hash;
        }
    }


    /** */
    static final class NullCommitOperation implements CommitOperation {

        /*********************************************************************\
         *  Constructors
        \*********************************************************************/

        /** Empty constructor. */
        NullCommitOperation() {}


        /*********************************************************************\
         *  Public Methods
        \*********************************************************************/

        /** */
        @Override
        public void commit() {}

        /** */
        @Override
        public void undo() {}
    }
}
