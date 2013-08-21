package com.github.undo4j.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.Injectable;

import org.junit.Before;
import org.junit.Test;

import com.github.undo4j.common.LockManager;
import com.github.undo4j.resources.AbstractResource;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.ResourceState;

public class AbstractResourceTest {

	@Injectable
	private InternalResource<String> internalResource;

	@Injectable
	private LockManager lockManager;

	private AsbtractResourceForTesting absResource;

	@Before
	public void setup() {
		absResource = new AsbtractResourceForTesting(internalResource, lockManager);
	}

	@Test
	public void testGetIsolation() throws Exception {
		assertNull(absResource.getIsolationLevel());
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

	private class AsbtractResourceForTesting extends AbstractResource<String> {

		public AsbtractResourceForTesting(InternalResource<String> r, LockManager lm) {
			super(r, lm);
		}

		@Override
		public ResourceState<String> read() {
			return null;
		}

		@Override
		public void write(ResourceState<String> state) {
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
		public AbstractResource<String> clone() {
			return null;
		}

	}
}
