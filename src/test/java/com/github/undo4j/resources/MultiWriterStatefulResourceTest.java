package com.github.undo4j.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.NonStrict;

import org.junit.Before;
import org.junit.Test;

import com.github.undo4j.common.LockManager;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.MultiWriterStatefulResource;
import com.github.undo4j.resources.NullState;
import com.github.undo4j.resources.ResourceState;
import com.github.undo4j.resources.MultiWriterStatefulResource.ThreadLocalResourceState;
import com.github.undo4j.resources.MultiWriterStatefulResource.ThreadLocalStatus;
import com.github.undo4j.resources.StatefulResource.Status;

public class MultiWriterStatefulResourceTest {

    @Injectable
    private LockManager lockManager;

    @Injectable
    private InternalResource<String> resource;

    private MultiWriterStatefulResourceForTesting multiWriterStatefulResource;

    @Injectable
    private ResourceState<String> stringResource;

    @Before
    public void setup() {
        multiWriterStatefulResource = new MultiWriterStatefulResourceForTesting(resource, lockManager);
    }

    @Test
    public void testGetLocalComit() throws Exception {
        ThreadLocalResourceState<?> localCommit = Deencapsulation.getField(multiWriterStatefulResource, "localCommit");
        assertNotSame(localCommit.get(), multiWriterStatefulResource.getLocalCommit());
    }

    @Test
    public void testGetLocalComitReference() throws Exception {
        ThreadLocalResourceState<?> localCommit = Deencapsulation.getField(multiWriterStatefulResource, "localCommit");
        assertSame(localCommit.get(), multiWriterStatefulResource.getLocalCommitReference());
    }

    @Test
    public void testGetThreadedLocalComit() throws Exception {
        ThreadLocalResourceState<?> localCommit = Deencapsulation.getField(multiWriterStatefulResource, "localCommit");
        assertSame(localCommit, multiWriterStatefulResource.getThreadLocalCommit());
    }

    @Test
    public void testGetSynchronizedResource() throws Exception {
        assertSame(resource, multiWriterStatefulResource.getSynchronizedResource());
    }

    @Test
    public void testSetLocalCommitNull() throws Exception {
        multiWriterStatefulResource.setLocalCommit(null);
        assertTrue(multiWriterStatefulResource.getLocalCommit() instanceof NullState);
    }

    @Test
    public void testSetLocalCommitNotNull(@NonStrict
    final ResourceState<String> rs) throws Exception {
        multiWriterStatefulResource.setLocalCommit(rs);
        ThreadLocalResourceState<?> localCommit = Deencapsulation.getField(multiWriterStatefulResource, "localCommit");
        assertSame(localCommit.get(), rs);
    }

    @Test
    public void testSetStatus() throws Exception {
        Status initialStatus = Status.initialStatus();
        multiWriterStatefulResource.setStatus(initialStatus);
        ThreadLocalStatus status = Deencapsulation.getField(multiWriterStatefulResource, "status");
        assertEquals(initialStatus, status.get());
    }

    @Test
    public void tetsHasLocalCommitNull() throws Exception {
        assertFalse(multiWriterStatefulResource.hasLocalCommit());
    }

    @Test
    public void tetsHasLocalCommitNotNull(@NonStrict
    final ResourceState<String> rs) throws Exception {
        multiWriterStatefulResource.setLocalCommit(rs);
        assertTrue(multiWriterStatefulResource.hasLocalCommit());
    }

    @Test
    public void testIsUpdated() throws Exception {
        multiWriterStatefulResource.setStatus(Status.UPDATED);
        assertTrue(multiWriterStatefulResource.isUpdated());
    }

    @Test
    public void testIsNotChanged() throws Exception {
        assertFalse(multiWriterStatefulResource.isChanged());
    }

    @Test
    public void testIsChanged() throws Exception {
        multiWriterStatefulResource.setStatus(Status.CHANGED);
        assertTrue(multiWriterStatefulResource.isChanged());
    }

    @Test
    public void testIsNotCommitted() throws Exception {
        assertFalse(multiWriterStatefulResource.isCommitted());
    }

    @Test
    public void testIsCommitted() throws Exception {
        multiWriterStatefulResource.setStatus(Status.COMMITTED);
        assertTrue(multiWriterStatefulResource.isCommitted());
    }

    @Test
    public void testUpdateNotCommitted() throws Exception {
        ResourceState<String> before = Deencapsulation.getField(multiWriterStatefulResource, "checkpoint");
        multiWriterStatefulResource.update();
        ResourceState<String> after = Deencapsulation.getField(multiWriterStatefulResource, "checkpoint");
        assertEquals(before, after);
    }

    @Test
    public void testUpdateNoLocalCommit() throws Exception {
        multiWriterStatefulResource.setStatus(Status.COMMITTED);
        ResourceState<String> before = Deencapsulation.getField(multiWriterStatefulResource, "checkpoint");
        multiWriterStatefulResource.update();
        ResourceState<String> after = Deencapsulation.getField(multiWriterStatefulResource, "checkpoint");
        assertEquals(before, after);
    }

    @Test
    public void testUpdate(@NonStrict
    final ResourceState<String> rs) throws Exception {
        new Expectations() {
            {
                rs.clone();
                result = rs;
                minTimes = 1;

            }
        };

        multiWriterStatefulResource.setStatus(Status.COMMITTED);
        multiWriterStatefulResource.setLocalCommit(rs);
        multiWriterStatefulResource.setCheckpoint(stringResource);
        ResourceState<String> beforeCheckPoint = Deencapsulation.getField(multiWriterStatefulResource, "checkpoint");
        multiWriterStatefulResource.update();
        ResourceState<String> afterCheckPoint = Deencapsulation.getField(multiWriterStatefulResource, "checkpoint");
        ResourceState<String> afterPrevious = Deencapsulation.getField(multiWriterStatefulResource, "previous");

        assertNotEquals(beforeCheckPoint, afterCheckPoint);
        assertSame(beforeCheckPoint, afterPrevious);
        assertTrue(multiWriterStatefulResource.isConsistent());
    }

    @Test
    public void testRollBackToCheckpoint(@NonStrict
    final ResourceState<String> rs) throws Exception {
        Deencapsulation.setField(multiWriterStatefulResource, "checkpoint", rs);
        new Expectations() {
            {
                InternalResource<String> resource = Deencapsulation.getField(multiWriterStatefulResource, "resource");
                ResourceState<String> checkpoint = Deencapsulation.getField(multiWriterStatefulResource, "checkpoint");
                resource.applyState(checkpoint);
                minTimes = 1;
            }
        };

        multiWriterStatefulResource.rollbackToCheckpoint();
        assertTrue(multiWriterStatefulResource.isConsistent());
    }

    @Test
    public void testRollBackToPrevious(@NonStrict
    final ResourceState<String> rs) throws Exception {
        Deencapsulation.setField(multiWriterStatefulResource, "previous", rs);
        new Expectations() {
            {
                InternalResource<String> resource = Deencapsulation.getField(multiWriterStatefulResource, "resource");
                ResourceState<String> previous = Deencapsulation.getField(multiWriterStatefulResource, "previous");
                resource.applyState(previous);
                minTimes = 1;
            }
        };

        multiWriterStatefulResource.rollbackToPrevious();
        assertTrue(multiWriterStatefulResource.isConsistent());
    }

    @Test
    public void testRemoveStatus() throws Exception {
        ThreadLocalStatus statusBefore = Deencapsulation.getField(multiWriterStatefulResource, "status");
        statusBefore.set(Status.CHANGED);
        multiWriterStatefulResource.removeStatus();
        ThreadLocalStatus statusAfter = Deencapsulation.getField(multiWriterStatefulResource, "status");

        Status actual = statusAfter.get();
        assertEquals(actual, Status.UPDATED);
    }

    @Test
    public void testRemoveLocalCommit(@NonStrict
    final ResourceState<String> rs) throws Exception {
        ThreadLocalResourceState<String> localCommitBefore = Deencapsulation.getField(multiWriterStatefulResource,
            "localCommit");
        localCommitBefore.set(rs);
        multiWriterStatefulResource.removeLocalCommit();
        ThreadLocalResourceState<?> localCommitAfter = Deencapsulation.getField(multiWriterStatefulResource,
            "localCommit");

        assertNotSame(rs, localCommitAfter.get());
    }

    @Test
    public void testApplyLocalCommit(@NonStrict
    final ResourceState<String> rs) throws Exception {
        new Expectations() {
            {
                InternalResource<String> resource = Deencapsulation.getField(multiWriterStatefulResource, "resource");
                resource.applyState(rs);
                minTimes = 1;
            }
        };
        multiWriterStatefulResource.applyState(rs);
    }

    @Test
    public void testApplyLocalCommit() throws Exception {
        new Expectations() {
            {

                InternalResource<String> resource = Deencapsulation.getField(multiWriterStatefulResource, "resource");
                resource.applyState(multiWriterStatefulResource.getLocalCommit());
                minTimes = 1;
            }
        };
        Deencapsulation.invoke(multiWriterStatefulResource, "applyLocalCommit");
    }

    private class MultiWriterStatefulResourceForTesting extends MultiWriterStatefulResource<String> {

        public MultiWriterStatefulResourceForTesting(InternalResource<String> resource, LockManager lockManager) {
            super(resource, lockManager);
        }

        @Override
        public void write(ResourceState<String> state) {}

        @Override
        public void commit() {}

        @Override
        public void rollback() {}

        @Override
        public MultiWriterStatefulResource<String> clone() {
            return null;
        }

    }
}
