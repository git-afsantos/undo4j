package org.bitbucket.jtransaction.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.bitbucket.jtransaction.common.IsolationLevel;
import org.bitbucket.jtransaction.common.LockManager;
import org.junit.Test;

public class AbstractResourceTest {

    @Injectable
    private InternalResource<?> internalResource;

    @Injectable
    private LockManager lockManager;

    @Tested
    private AbstractResource<?> absResource;

    @Test
    public void testGetIsolation() throws Exception {
        assertEquals(IsolationLevel.NONE, absResource.getIsolationLevel());
    }

    @Test
    public void testGetInternalResource() throws Exception {
        assertEquals(internalResource, absResource.getInternalResource());
    }

    @Test
    public void testGetSynchronizedResource() throws Exception {
        assertEquals(internalResource, absResource.getSynchronizedResource());
    }

    @Test
    public void testSetGetAccessible() throws Exception {
        absResource.setAccessible(true);

        assertTrue(absResource.isAccessible());
    }

    @Test
    public void testSetGetConsistent() throws Exception {
        absResource.setConsistent(false);

        assertFalse(absResource.isConsistent());
    }

    @Test
    public void testInitialize() throws Exception {
        new Expectations() {
            {
                internalResource.initialize();

                absResource.initializeDecorator();
            }
        };
        absResource.initialize();

        assertTrue(absResource.isAccessible());
    }

    @Test
    public void testDispose() throws Exception {
        new Expectations() {
            {
                internalResource.dispose();

                absResource.disposeDecorator();
            }
        };
        absResource.dispose();

        assertFalse(absResource.isAccessible());
    }

}
