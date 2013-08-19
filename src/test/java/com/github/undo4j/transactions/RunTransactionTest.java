package com.github.undo4j.transactions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

import com.github.undo4j.common.LockManagers;
import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.ResourceState;
import com.github.undo4j.resources.ShadowResource;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionManager;
import com.github.undo4j.transactions.TransactionManagers;
import com.github.undo4j.transactions.TransactionalCallable;

public final class RunTransactionTest {

    @Test
    public void testNormalTransaction() throws InterruptedException, ExecutionException {

        ManagedResource<String> mr = ManagedResource.from(new ShadowResource<String>(new StringResource(), LockManagers
            .newNullLockManager()));
        TransactionManager tm = TransactionManagers.newSynchronousManager();
        Future<String> f = tm.submit(new WriteTransaction("Hello", mr));
        String s = f.get();

        Assert.assertEquals("Hello", s);
    }

    @Test(expected = ExecutionException.class)
    public void testResourceHidingEmptyTransaction() throws Exception {

        ManagedResource<String> mr = ManagedResource.from(new ShadowResource<String>(new StringResource(), LockManagers
            .newNullLockManager()));

        TransactionManager tm = TransactionManagers.newSynchronousManager();
        Future<String> f = tm.submit(new ResourceHidingEmptyTransaction("Hello", mr));
        f.get();
    }

    @Test(expected = ExecutionException.class)
    public void testResourceHidingTransaction() throws Exception {

        ManagedResource<String> mr = ManagedResource.from(new ShadowResource<String>(new StringResource(), LockManagers
            .newNullLockManager()));
        ManagedResource<String> mrHidden = ManagedResource.from(new ShadowResource<String>(new StringResource(),
            LockManagers.newNullLockManager()));
        TransactionManager tm = TransactionManagers.newSynchronousManager();
        Future<String> f = tm.submit(new ResourceHidingTransaction("Hello", mr, mrHidden));
        f.get();
    }

    class WriteTransaction implements TransactionalCallable<String> {
        private final String text;
        private ManagedResource<String> resource;

        /** */
        public WriteTransaction(String s, ManagedResource<String> r) {
            text = s;
            resource = r;
        }

        @Override
        public String call() throws Exception {
            resource.write(new ImmutableState<String>(text));
            return text;
        }

        @Override
        public Iterable<ManagedResource<?>> getManagedResources() {
            List<ManagedResource<?>> list = new ArrayList<>();
            list.add(resource);
            return list;
        }

    }

    class StringResource implements InternalResource<String> {
        // instance variables
        private String myString = "";

        @Override
        public boolean isValidState(ResourceState<String> state) {
            return !state.isNull();
        }

        @Override
        public ResourceState<String> buildState() {
            return new ImmutableState<String>(myString);
        }

        @Override
        public void applyState(ResourceState<String> state) {
            myString = state.get();
        }

    }

    class ResourceHidingTransaction implements TransactionalCallable<String> {
        private final String text;
        private ManagedResource<String> resource, hidden;

        public ResourceHidingTransaction(String s, ManagedResource<String> r, ManagedResource<String> h) {
            text = s;
            resource = r;
            hidden = h;
        }

        @Override
        public String call() throws Exception {
            resource.write(new ImmutableState<String>(text));
            hidden.write(new ImmutableState<String>(text));
            return text;
        }

        @Override
        public Iterable<ManagedResource<?>> getManagedResources() {
            List<ManagedResource<?>> l = new ArrayList<>();
            l.add(resource);
            return l;
        }
    }

    public class ResourceHidingEmptyTransaction implements TransactionalCallable<String> {
        private final String text;
        private ManagedResource<String> resource;

        /** */
        public ResourceHidingEmptyTransaction(String s, ManagedResource<String> r) {
            text = s;
            resource = r;
        }

        @Override
        public String call() throws Exception {
            resource.write(new ImmutableState<String>(text));
            return text;
        }

        @Override
        public Iterable<ManagedResource<?>> getManagedResources() {
            return null;
        }

    }
}
