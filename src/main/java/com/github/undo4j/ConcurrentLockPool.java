package com.github.undo4j;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ConcurrentLockPool
 * 
 * @author afs
 * @version 2013
 */

final class ConcurrentLockPool extends LockPool
        implements ResourceLock.AcquireListener {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** Internal lock. */
    private final Lock internalLock = new ReentrantLock();

    /** TODO: replace for R -> {(owners, waiting)} */
    private final Map<ResourceId, Set<TransactionId>> acquired =
        new HashMap<>();

    /** */
    private final Map<ResourceId, Set<TransactionId>> waiting =
        new HashMap<>();

    /** */
    private final WaitForGraph graph = new WaitForGraph();



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class ConcurrentLockPool.
     */
    ConcurrentLockPool() {}



    /*************************************************************************\
     *  WaitListener Methods
    \*************************************************************************/

    /** */
    @Override
    public void waiting(final TransactionId tid, final ResourceId resource) {
        waiting.get(resource).add(tid);
        for (TransactionId i: acquired.get(resource)) {
            graph.put(tid, i);
        }
    }


    /** */
    @Override
    public void notWaiting(final TransactionId tid, final ResourceId resource) {
        waiting.get(resource).remove(tid);
        graph.removeKey(tid);
    }


    /** */
    @Override
    public void acquired(final TransactionId tid, final ResourceId resource) {
        acquired.get(resource).add(tid);
        for (TransactionId i: waiting.get(resource)) {
            graph.put(i, tid);
        }
    }


    /** */
    @Override
    public void released(final TransactionId tid, final ResourceId resource) {
        acquired.get(resource).remove(tid);
        graph.removeValue(tid);
    }



    /*************************************************************************\
     *  ResourceManager Methods
    \*************************************************************************/

    /** */
    @Override
    public void acquire(
            final TransactionId tid,
            final ResourceId key,
            final AccessMode mode,
            final WaitStrategy strategy) {
        internalLock.lock();
        try     { super.acquire(tid, key, mode, strategy); }
        finally { internalLock.unlock(); }
    }


    /** */
    @Override
    public void release(final TransactionId tid, final ResourceId key) {
        internalLock.lock();
        try     { super.release(tid, key); }
        finally { internalLock.unlock(); }
    }



    /*************************************************************************\
     *  Package Methods
    \*************************************************************************/

    /** */
    @Override
    protected void put(final IsolationLevel isolation, final ResourceId key) {
        assert isolation != null && key != null;
        internalLock.lock();
        try {
            super.checkNotRegistered(key);
            super.locks.put(key,
                ResourceLocks.newLock(isolation, key, this, internalLock));
            acquired.put(key, new HashSet<TransactionId>());
            waiting.put(key, new HashSet<TransactionId>());
        } finally { internalLock.unlock(); }
    }


    /** */
    @Override
    protected void remove(final ResourceId key) {
        internalLock.lock();
        try {
            super.remove(key);
            acquired.remove(key);
            waiting.remove(key);
        } finally { internalLock.unlock(); }
    }

    /** */
    @Override
    protected void remove(final Iterable<ResourceId> keys) {
        internalLock.lock();
        try {
            for (ResourceId key: keys) {
                super.remove(key);
                acquired.remove(key);
                waiting.remove(key);
            }
        } finally { internalLock.unlock(); }
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /** */
    private static final class WaitForGraph {
        private final Map<TransactionId, Set<TransactionId>> keyValue =
            new HashMap<>();
        private final Map<TransactionId, Set<TransactionId>> valueKey =
            new HashMap<>();

        /** */
        void put(final TransactionId key, final TransactionId value) {
            checkNoPath(value, key);
            putKey(keyValue, key).add(value);
            putKey(valueKey, value).add(key);
        }

        /** */
        void removeKey(final TransactionId key) {
            Set<TransactionId> set = keyValue.remove(key);
            if (set != null) {
                for (TransactionId value: set) {
                    valueKey.get(value).remove(key);
                }
            }
        }

        /** */
        void removeValue(final TransactionId value) {
            Set<TransactionId> set = valueKey.remove(value);
            if (set != null) {
                for (TransactionId key: set) {
                    keyValue.get(key).remove(value);
                }
            }
        }


        /** */
        private void checkNoPath
                (final TransactionId from, final TransactionId to) {
            Set<TransactionId> visited = new HashSet<>();
            Deque<TransactionId> stack = new LinkedList<>();
            stack.addLast(from);
            while (!stack.isEmpty()) {
                final TransactionId tid = stack.removeLast();
                if (visited.add(tid)) {
                    if (keyValue.containsKey(tid)) {
                        for (final TransactionId adj: keyValue.get(tid)) {
                            if (adj.equals(to)) {
                                throw new DeadlockDetectedException
                                    ("Deadlock between: " + from + " and " + to);
                            }
                            stack.addLast(adj);
                        }
                    }
                }
            }
        }


        /** */
        private static Set<TransactionId> putKey(
                final Map<TransactionId, Set<TransactionId>> map,
                final TransactionId key) {
            Set<TransactionId> set = map.get(key);
            if (set == null) {
                set = new HashSet<>();
                map.put(key, set);
            }
            return set;
        }
    }
}
