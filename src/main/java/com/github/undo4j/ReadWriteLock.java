package com.github.undo4j;

import java.util.concurrent.locks.Condition;


/**
 * WARNING: not reentrant
 * 
 * @author afs
 * @version 2014-01-29
 */

final class ReadWriteLock extends ResourceLock {

    /*************************************************************************\
     *  Enums
    \*************************************************************************/

    /** */
    private static enum State { FREE, READER, WRITER }



    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** */
    private final Condition readCondition, writeCondition;

    /** */
    private State state = State.FREE;

    /** */
    private int waitingReaders  = 0;

    /** */
    private int waitingWriters  = 0;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class ReadWriteLock.
     */
    ReadWriteLock(
            final ResourceId id,
            final Condition readCondition,
            final Condition writeCondition) {
        super(id);
        assert readCondition != null && writeCondition != null;
        this.readCondition = readCondition;
        this.writeCondition = writeCondition;
    }


    /**
     *  Parameter constructor of objects of class ReadWriteLock.
     */
    ReadWriteLock(
            final ResourceId id,
            final AcquireListener listener,
            final Condition readCondition,
            final Condition writeCondition) {
        super(id, listener);
        assert readCondition != null && writeCondition != null;
        this.readCondition = readCondition;
        this.writeCondition = writeCondition;
    }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    protected IsolationLevel getIsolationLevel() {
        return IsolationLevel.READ_WRITE;
    }


    /** */
    @Override
    protected boolean acquire(
            final TransactionId tid,
            final AccessMode mode,
            final WaitStrategy strat) throws InterruptedException {
        assert tid != null && mode != null && strat != null;
        // Fail if lock is invalid.
        if (!super.isValid()) { return false; }
        // Register ownership and state, if acquired.
        switch (mode) {
            case READ:  return acquireRead(tid, strat);
            case WRITE: return acquireWrite(tid, strat);
            default:    throw new AssertionError();
        }
    }


    /** */
    @Override
    protected void release(final TransactionId tid) {
        assert tid != null && super.hasOwner();
        // Clear ownership.
        super.notifyReleased(tid);
        // Wake up next contenders.
        switch (state) {
            case READER:    releaseRead(tid);   return;
            case WRITER:    releaseWrite(tid);  return;
            default:        throw new AssertionError();
        }
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */
    private boolean acquireRead
        (final TransactionId tid, final WaitStrategy strat)
            throws InterruptedException {
        boolean success = true;
        // Acquire instantly if there are no writers, wait otherwise.
        while (success && state == State.WRITER) {
            ++waitingReaders;
            super.notifyWaiting(tid);
            try { success = strat.waitOn(readCondition); }
            catch (InterruptedException ex) {
                if (state == State.FREE) {
                    if (waitingReaders > 1) { readCondition.signal(); }
                    else if (waitingWriters > 0) { writeCondition.signal(); }
                }
                throw ex;
            } finally {
                --waitingReaders;
                super.notifyNotWaiting(tid);
            }
        }
        if (success) {
            state = State.READER;
            super.notifyAcquired(tid);
        }
        return success;
    }


    /** */
    private boolean acquireWrite
        (final TransactionId tid, final WaitStrategy strat)
            throws InterruptedException {
        boolean success = true;
        while (success && state != State.FREE) {
            // Wait for turn.
            ++waitingWriters;
            super.notifyWaiting(tid);
            try { success = strat.waitOn(writeCondition); }
            catch (InterruptedException ex) {
                if (state == State.FREE) {
                    if (waitingWriters > 1) { writeCondition.signal(); }
                    else if (waitingReaders > 0) { readCondition.signal(); }
                }
                throw ex;
            } finally {
                --waitingWriters;
                super.notifyNotWaiting(tid);
            }
        }
        if (success) {
            state = State.WRITER;
            super.notifyAcquired(tid);
        }
        return success;
    }


    /** */
    private void releaseRead(final TransactionId tid) {
        // Only the last reader acts.
        if (!super.hasOwner()) {
            state = State.FREE;
            // Prefer writers.
            if      (waitingWriters > 0) { writeCondition.signal(); }
            else if (waitingReaders > 0) { readCondition.signalAll(); }
        }
    }


    /** */
    private void releaseWrite(final TransactionId tid) {
        assert !super.hasOwner();
        state = State.FREE;
        // Prefer readers.
        if      (waitingReaders > 0) { readCondition.signalAll(); }
        else if (waitingWriters > 0) { writeCondition.signal(); }
    }
}
