package com.github.undo4j.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.NonStrict;

import org.junit.Before;
import org.junit.Test;

import com.github.undo4j.common.LockManager;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.ResourceState;
import com.github.undo4j.resources.SingleWriterStatefulResource;
import com.github.undo4j.resources.StatefulResource.Status;

public class SingleWriterStatefulResourceTest {
	@Injectable
	private InternalResource<String> resource;

	@Injectable
	private LockManager lockManager;

	private SingleWriterStatefulResourceForTesting swsr;

	@Before
	public void setup() {
		swsr = new SingleWriterStatefulResourceForTesting(resource, lockManager);
	}

	@Test
	public void setgetLocalCommitTest(@NonStrict final ResourceState<String> mock) throws Exception {
		swsr.setLocalCommit(mock);
		assertTrue(swsr.hasLocalCommit());
	}

	@Test
	public void setgetLocalCommitReferenceTest(@NonStrict final ResourceState<String> mock) throws Exception {
		swsr.setLocalCommit(mock);
		assertEquals(mock, swsr.getLocalCommitReference());
	}

	@Test
	public void setgetStatusTest(@NonStrict final Status mock) {
		swsr.setStatus(mock);
		assertEquals(mock, swsr.getStatus());
	}

	@Test
	public void writeUpdateTest(@NonStrict final ResourceState<String> mock) throws Exception {
		final InternalResource<String> ir2 = new MockUp<InternalResource<String>>() {
			@Mock
			boolean isValidState(ResourceState<String> s) {
				return true;
			}
		}.getMockInstance();

		new Expectations() {
			{
				swsr.updatePreviousCheckpoint();
				swsr.setCheckpoint(mock);
			}
		};
		Deencapsulation.setField(swsr, "resource", ir2);
		swsr.setLocalCommit(mock);
		swsr.update();
		assertEquals(Status.UPDATED, swsr.getStatus());
	}

	private class SingleWriterStatefulResourceForTesting extends SingleWriterStatefulResource<String> {

		public SingleWriterStatefulResourceForTesting(InternalResource<String> resource, LockManager lockManager) {
			super(resource, lockManager);
		}

		@Override
		public void commit() {
		}

		@Override
		public void rollback() {
		}

		@Override
		public SingleWriterStatefulResource<String> clone() {
			return null;
		}

		@Override
		public void write(ResourceState<String> state) {
		}

	}
}
