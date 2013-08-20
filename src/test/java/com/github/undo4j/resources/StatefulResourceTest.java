package com.github.undo4j.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.NonStrict;

import org.junit.Before;
import org.junit.Test;

import com.github.undo4j.common.LockManager;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.NullState;
import com.github.undo4j.resources.ResourceRollbackException;
import com.github.undo4j.resources.ResourceState;
import com.github.undo4j.resources.ResourceUpdateException;
import com.github.undo4j.resources.StatefulResource;

public class StatefulResourceTest {

    private final InternalResource<String> irEx = new MockUp<InternalResource<String>>() {
        @Mock
        ResourceState<String> buildState() throws Exception {
            throw new Exception();
        }

        @Mock
        void applyState(ResourceState<String> state) throws Exception {
            throw new Exception();
        }
    }.getMockInstance();

    @Injectable
    private InternalResource<String> resource;

    @Injectable
    private LockManager lockManager;

    private StatefulResourceForTesting srt;

    @Before
    public void setup() {
        srt = new StatefulResourceForTesting(resource, lockManager);
    }

    @Test
    public void setgetCheckpointTest(@NonStrict
    final ResourceState<String> mock) throws Exception {
        srt.setCheckpoint(mock);
        assertTrue(srt.hasCheckpoint());
    }

    @Test
    public void setgetPreviousCheckpointTest(@NonStrict
    final ResourceState<String> mock) throws Exception {
        srt.setPreviousCheckpoint(mock);
        assertTrue(srt.hasPreviousCheckpoint());

    }

    @Test
    public void setgetCheckpointReferenceTestNotNull(@NonStrict
    final ResourceState<String> mock) {
        srt.setCheckpoint(mock);
        assertEquals(mock, srt.getCheckpointReference());
    }

    @Test
    public void setgetCheckpointReferenceTestNull(@NonStrict
    final ResourceState<String> mock) throws Exception {
        srt.setCheckpoint(null);
        assertEquals(new NullState<String>(), srt.getCheckpointReference());
    }

    @Test
    public void setgetPrevCheckpointReferenceTestNotNull(@NonStrict
    final ResourceState<String> mock) {
        srt.setPreviousCheckpoint(mock);
        assertEquals(mock, srt.getPreviousCheckpointReference());
    }

    @Test
    public void setgetPrevCheckpointReferenceTestNull(@NonStrict
    final ResourceState<String> mock) throws Exception {
        srt.setPreviousCheckpoint(null);
        assertEquals(new NullState<String>(), srt.getPreviousCheckpointReference());
    }

    @Test
    public void updatePreviousCheckpointTest(@NonStrict
    final ResourceState<String> mock) throws Exception {
        srt.setCheckpoint(mock);
        srt.updatePreviousCheckpoint();
        assertEquals(mock, srt.getCheckpointReference());
    }

    @Test
    public void revertCheckpointTest(@NonStrict
    final ResourceState<String> mock) throws Exception {
        srt.setPreviousCheckpoint(mock);
        srt.revertCheckpoint();
        assertEquals(mock, srt.getPreviousCheckpointReference());
    }

    @Test
    public void updateCheckpointTestException() throws Exception {
        Deencapsulation.setField(srt, "resource", irEx);
        try {
            srt.updateCheckpoint();
            fail();
        } catch (ResourceUpdateException e) {
            assertFalse(srt.isConsistent());
        }

    }

    @Test
    public void updateCheckpointTest() throws Exception {
        new Expectations() {
            {
                resource.buildState();
            }
        };

        srt.updateCheckpoint();
    }

    @Test
    public void rollbackToCheckpointTest(@NonStrict
    final ResourceState<String> mock) throws Exception {
        new Expectations() {
            {
                resource.applyState(mock);
            }
        };

        srt.rollbackToCheckpoint();
    }

    public void rollbackToCheckpointTestException() throws Exception {
        Deencapsulation.setField(srt, "resource", irEx);
        try {
            srt.rollbackToCheckpoint();
            fail();
        } catch (ResourceRollbackException e) {
            assertFalse(srt.isConsistent());
        }
    }

    private class StatefulResourceForTesting extends StatefulResource<String> {

        public StatefulResourceForTesting(InternalResource<String> resource, LockManager lockManager) {
            super(resource, lockManager);
        }

        @Override
        public void write(ResourceState<String> state) {}

        @Override
        public void commit() {}

        @Override
        public void rollback() {}

        @Override
        public void update() {}

        @Override
        public StatefulResource<String> clone() {
            return null;
        }

    }
}