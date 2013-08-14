package org.bitbucket.jtransaction.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;

import org.bitbucket.jtransaction.common.LockManager;
import org.bitbucket.jtransaction.resources.StatefulResource.Status;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ShadowResourceTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ShadowResourceTest {

    private ShadowResource<String> resource;

    @Injectable
    private LockManager lockManager;

    @Injectable
    private InternalResource<String> internalResource;

    @Injectable
    private ResourceState<String> stringResource1;
    @Injectable
    private ResourceState<String> stringResource2;
    @Injectable
    private ResourceState<String> stringResource3;

    @Before
    public void setUp() {
        resource = new ShadowResource<String>(internalResource, lockManager);
        resource.initialize();
    }

    @Test
    public void testGetShadow() throws Exception {
        ResourceState<String> shadow = resource.getShadow();
        assertNotSame(stringResource1, shadow);
    }

    @Test
    public void testGetShadowReference() throws Exception {
        ResourceState<String> shadow = resource.getShadow();
        assertNotSame(stringResource1, shadow);
    }

    @Test
    public void testSetShadowNull() throws Exception {
        resource.setShadow(null);
        assertTrue(resource.getShadowReference() instanceof NullState);
    }

    @Test
    public void testHasShadowNull() throws Exception {
        resource.setShadow(null);
        assertFalse(resource.hasShadow());
    }

    @Test
    public void testHasShadow() throws Exception {
        resource.setShadow(stringResource2);
        assertTrue(resource.hasShadow());
    }

    @Test
    public void testWrite() throws Exception {
        resource.write(stringResource1);

        assertSame(stringResource1, resource.getShadowReference());
        assertEquals(Status.CHANGED, resource.getStatus());
    }

    @Test
    public void testCommitUnChanged() throws Exception {
        resource.setConsistent(false);
        resource.setStatus(Status.UPDATED);
        resource.commit();

        assertEquals(Status.UPDATED, resource.getStatus());
        assertFalse(resource.isConsistent());
    }

    @Test
    public void testCommitNoShadow() throws Exception {
        resource.setConsistent(false);
        resource.setStatus(Status.CHANGED);
        resource.setShadow(null);
        resource.commit();

        assertEquals(Status.COMMITTED, resource.getStatus());
        assertTrue(resource.isConsistent());
    }

    @Test
    public void testCommit() throws Exception {
        ResourceState<?> localCommit = Deencapsulation.getField(resource, "localCommit");
        resource.setConsistent(false);
        resource.setStatus(Status.CHANGED);
        resource.setShadow(stringResource1);
        resource.commit();

        assertEquals(Status.COMMITTED, resource.getStatus());
        assertTrue(resource.isConsistent());
        assertNotSame(localCommit, stringResource1);
        assertTrue(resource.getShadow() instanceof NullState);
    }

    @Test
    public void testRollbackStatusCommitted() throws Exception {
        resource.setStatus(Status.COMMITTED);
        resource.setLocalCommit(stringResource1);
        resource.setCheckpoint(stringResource2);
        new Expectations() {
            {
                resource.rollbackToCheckpoint();
                minTimes = 1;
            }
        };

        resource.rollback();

        assertTrue(resource.getShadow() instanceof NullState);
        assertTrue(resource.getLocalCommit() instanceof NullState);
        assertEquals(Status.UPDATED, resource.getStatus());
        assertTrue(resource.isConsistent());

    }

    @Test
    public void testRollbackStatusUpdated() throws Exception {
        resource.setStatus(Status.COMMITTED);
        resource.setLocalCommit(stringResource1);
        resource.setCheckpoint(stringResource2);
        resource.setPreviousCheckpoint(stringResource3);
        new Expectations() {
            {
                resource.rollbackToPrevious();
                minTimes = 1;
            }
        };

        resource.rollback();

        assertTrue(resource.getShadow() instanceof NullState);
        assertTrue(resource.getLocalCommit() instanceof NullState);
        assertEquals(Status.UPDATED, resource.getStatus());
        assertTrue(resource.isConsistent());
    }

    @Test
    public void testRollbackStatusChanged() throws Exception {
        resource.setStatus(Status.CHANGED);
        resource.setLocalCommit(stringResource1);
        resource.setCheckpoint(stringResource2);
        resource.setPreviousCheckpoint(stringResource3);
        resource.rollback();

        assertTrue(resource.getShadow() instanceof NullState);
        assertTrue(resource.getLocalCommit() instanceof NullState);
        assertEquals(Status.UPDATED, resource.getStatus());
        assertTrue(resource.isConsistent());
    }

    @Test
    public void testDisposeDecorator() throws Exception {
        resource.setCheckpoint(stringResource2);
        resource.disposeDecorator();

        assertTrue(resource.getCheckpoint() instanceof NullState);
        assertEquals(resource.getPreviousCheckpoint(), stringResource2);
        assertEquals(resource.getLocalCommitReference(), stringResource2);
        assertEquals(resource.getShadowReference(), stringResource2);
    }

    @Test
    public void testApplyShadow() throws Exception {
        new Expectations() {
            {
                internalResource.applyState(resource.getShadowReference());
                minTimes = 1;
            }
        };
        Deencapsulation.invoke(resource, "applyShadow");
    }
}
