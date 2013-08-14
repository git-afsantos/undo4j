package org.bitbucket.jtransaction.resources;

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

import org.bitbucket.jtransaction.common.LockManager;
import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void initializeDecoratorTest() throws Exception {
        new Expectations() {
            {
                resource.buildState();
            }
        };

        srt.initializeDecorator();
    }

    @Test
    public void initializeDecoratorTestException() {
        Deencapsulation.setField(srt, "resource", irEx);
        try {
            srt.initializeDecorator();
            fail();
        } catch (ResourceInitializeException e) {}
    }

    @Test
    public void disposeDecoratorTest() {
        srt.disposeDecorator();
        assertEquals(new NullState<String>(), srt.getCheckpointReference());
        assertEquals(new NullState<String>(), srt.getPreviousCheckpointReference());
    }

    private class StatefulResourceForTesting extends StatefulResource<String> {

        public StatefulResourceForTesting(InternalResource<String> resource, LockManager lockManager) {
            super(resource, lockManager);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void write(ResourceState<String> state) {
            // TODO Auto-generated method stub

        }

        @Override
        public void commit() {
            // TODO Auto-generated method stub

        }

        @Override
        public void rollback() {
            // TODO Auto-generated method stub

        }

        @Override
        public void update() {
            // TODO Auto-generated method stub

        }

        @Override
        public StatefulResource<String> clone() {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
