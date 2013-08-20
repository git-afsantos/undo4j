package com.github.undo4j.common;

import static com.github.undo4j.common.Check.checkNotNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * The test class CheckNotNullTest.
 *
 * @author  afs
 * @version 2013
 */
public class CheckNotNullTest {

    @Test
    public void testCheckNotNullWithNotNullObject() throws Exception {
        Object o = new Object();
        checkNotNull(o);

        assertNotNull(o);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckNotNullWithNullObject() throws Exception {
        checkNotNull(null);
    }
}
