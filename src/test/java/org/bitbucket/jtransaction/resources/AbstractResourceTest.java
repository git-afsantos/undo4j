package org.bitbucket.jtransaction.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;

import org.bitbucket.jtransaction.common.IsolationLevel;
import org.junit.Test;

public class AbstractResourceTest {

    private final InternalResource ir = new MockUp<InternalResource>() {}.getMockInstance();

    @Test
    public void testName() throws Exception {
        assertNotNull(ir);
    }

    @Test
    public void testConstructorInternalResource() throws Exception {
        AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);

        assertNotNull(absResource);
        assertFalse(absResource.isAccessible());
        assertTrue(absResource.isConsistent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInternalResourceNull() throws Exception {
        InternalResource ir = null;
        new AbstractResourceForTesting(ir);
    }

    @Test
    public void testConstructorAbstractResource() throws Exception {
        AbstractResource original = new AbstractResourceForTesting(ir);
        AbstractResourceForTesting copy = new AbstractResourceForTesting(original);

        assertNotNull(copy);
        assertEquals(original.isAccessible(), copy.isAccessible());
        assertEquals(original.isConsistent(), copy.isConsistent());
    }

    @Test
    public void testGetIsolation() throws Exception {
        AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);

        assertEquals(IsolationLevel.NONE, absResource.getIsolationLevel());
    }

    @Test
    public void testGetInternalResource() throws Exception {
        AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);

        assertEquals(ir, absResource.getInternalResource());
    }

    @Test
    public void testGetSynchronizedResource() throws Exception {
        AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);

        assertEquals(ir, absResource.getSynchronizedResource());
    }

    @Test
    public void testSetGetAccessible() throws Exception {
        AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);
        absResource.setAccessible(true);

        assertTrue(absResource.isAccessible());
    }

    @Test
    public void testSetGetConsistent() throws Exception {
        AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);
        absResource.setConsistent(false);

        assertFalse(absResource.isConsistent());
    }

    @Test
    public void testInitialize() throws Exception {
        final AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);
        new Expectations() {
            {
                ir.initialize();

                absResource.initializeDecorator();
            }
        };
        absResource.initialize();

        assertTrue(absResource.isAccessible());
    }

    @Test
    public void testDispose() throws Exception {
        final AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);
        new Expectations() {
            {
                ir.dispose();

                absResource.disposeDecorator();
            }
        };
        absResource.dispose();

        assertFalse(absResource.isAccessible());
    }

    @Test
    public void testTryAcquireForModeAndTime() throws Exception {
        AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);
        assertTrue(absResource.tryAcquireFor(null, 0L));
    }

    @Test
    public void testTryAcquireForMode() throws Exception {
        AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);
        assertTrue(absResource.tryAcquireFor(null));
    }

    @Test(expected = ResourceInitializeException.class)
    public void testInitializeInternalResource() throws Exception {
        InternalResource ir = new MockUp<InternalResource>() {
            @Mock
            void initialize() throws Exception {
                throw new Exception();
            }
        }.getMockInstance();
        AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);
        Deencapsulation.invoke(absResource, "initializeInternalResource");
    }

    @Test(expected = ResourceDisposeException.class)
    public void testDisposeInternalResource() throws Exception {
        InternalResource ir = new MockUp<InternalResource>() {
            @Mock
            void dispose() throws Exception {
                throw new Exception();
            }
        }.getMockInstance();
        AbstractResourceForTesting absResource = new AbstractResourceForTesting(ir);
        Deencapsulation.invoke(absResource, "disposeInternalResource");
    }

    private class AbstractResourceForTesting extends AbstractResource {

        public AbstractResourceForTesting(AbstractResource instance) {
            super(instance);
        }

        public AbstractResourceForTesting(InternalResource r) {
            super(r);
        }

        @Override
        public ResourceState read() {
            return null;
        }

        @Override
        public void write(ResourceState state) {

        }

        @Override
        public void commit() {

        }

        @Override
        public void rollback() {

        }

        @Override
        public void update() {

        }

        @Override
        public AbstractResource clone() {
            return null;
        }

    }
}
