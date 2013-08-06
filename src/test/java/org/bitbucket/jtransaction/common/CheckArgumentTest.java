package org.bitbucket.jtransaction.common;

import static org.bitbucket.jtransaction.common.Check.checkArgument;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The test class CheckArgumentTest.
 *
 * @author  afs
 * @version 2013
 */
public class CheckArgumentTest {
    /** */
    public CheckArgumentTest() {}

    /** */
    @Test
    public void testNullArgument() {
        try {
            checkArgument(null);
            fail("null argument passed non-null test");
        } catch (IllegalArgumentException ex) {
            assertTrue(true);
        }
    }

    /** */
    @Test
    public void testArgument() {
        Object o = new Object();
        try {
            checkArgument(o);
            if (o == null) { fail("null argument passed non-null test"); }
            else { assertTrue(true); }
        } catch (IllegalArgumentException ex) {
            if (o == null) { assertTrue(true); }
            else { fail("non-null argument failed non-null test"); }
        }
    }
}
