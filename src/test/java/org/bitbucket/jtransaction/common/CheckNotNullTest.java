package org.bitbucket.jtransaction.common;

import static org.bitbucket.jtransaction.common.Check.checkNotNull;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The test class CheckNotNullTest.
 *
 * @author  afs
 * @version 2013
 */
public class CheckNotNullTest {
    /** */
    public CheckNotNullTest() {}

    /** */
    @Test
    public void testNullIsNull() {
        try {
            checkNotNull(null);
            fail("null passed non-null check");
        } catch (NullPointerException ex) {
            assertTrue(true);
        }
    }

    /** */
    @Test
    public void testNotNull() {
        Object o = new Object();
        try {
            checkNotNull(o);
            if (o == null) { fail("null passed non-null check"); }
            else { assertTrue(true); }
        } catch (NullPointerException ex) {
            if (o == null) { assertTrue(true); }
            else { fail("non-null object failed non-null check"); }
        }
    }
}
