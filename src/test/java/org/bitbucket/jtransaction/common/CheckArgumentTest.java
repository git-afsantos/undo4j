package org.bitbucket.jtransaction.common;

import static org.bitbucket.jtransaction.common.Check.checkArgument;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * The test class CheckArgumentTest.
 *
 * @author  afs
 * @version 2013
 */
public class CheckArgumentTest {

    /** */
    @Test(expected = IllegalArgumentException.class)
    public void testNullArgument() {
        checkArgument(null);
    }

    /** */
    @Test
    public void testArgument() {
        Object o = new Object();
        checkArgument(o);

        assertNotNull(o);
    }
}
