package org.bitbucket.jtransaction.resources;

import org.bitbucket.jtransaction.resources.IntState;
import org.bitbucket.jtransaction.resources.ResourceState;
import org.bitbucket.jtransaction.resources.ShadowResource;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ShadowResourceTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ShadowResourceTest {
    private ShadowResource resource;

    /** */
    public ShadowResourceTest() {}

    /** */
    @Before
    public void setUp() {
        resource = new ShadowResource(new PassiveInternalResource());
        resource.initialize();
    }

    /** */
    @After
    public void tearDown() { resource = null; }



    /** */
    @Test
    public void read() {
        assertNotNull(resource.read());
    }

    /** */
    @Test
    public void writeAndCompare() {
        ResourceState s1 = resource.read();
        resource.write(new IntState());
        ResourceState s2 = resource.read();
        assertEquals(s1, s2);
    }

    /** */
    @Test
    public void commitAndCompare() {
        ResourceState s1 = resource.read();
        resource.write(new IntState());
        resource.commit();
        ResourceState s2 = resource.read();
        assertEquals(s1, s2);
    }

    /** */
    @Test
    public void updateAndCompare() {
        ResourceState s1 = resource.read();
        resource.write(new IntState());
        resource.commit();
        resource.update();
        ResourceState s2 = resource.read();
        assertTrue(s1 != s2);
    }

    /** */
    @Test
    public void rollbackAfterWrite() {
        ResourceState s1 = resource.read();
        resource.write(new IntState());
        resource.rollback();
        ResourceState s2 = resource.read();
        assertEquals(s1, s2);
    }

    /** */
    @Test
    public void rollbackAfterCommit() {
        ResourceState s1 = resource.read();
        resource.write(new IntState());
        resource.commit();
        resource.rollback();
        ResourceState s2 = resource.read();
        assertEquals(s1, s2);
    }

    /** */
    @Test
    public void rollbackAfterUpdate() {
        resource.write(new IntState());
        resource.commit();
        resource.update();
        ResourceState s1 = resource.read();
        resource.rollback();
        ResourceState s2 = resource.read();
        assertTrue(s1 != s2);
    }
}
