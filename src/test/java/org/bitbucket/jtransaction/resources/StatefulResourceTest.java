package org.bitbucket.jtransaction.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.NonStrict;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class StatefulResourceTest {

	private InternalResource ir = new MockUp<InternalResource>() {
	}.getMockInstance();

	@Test
	public void setgetCheckpointTest(@NonStrict final ResourceState mock)
			throws Exception {
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir);

		srt.setCheckpoint(mock);
		assertTrue(srt.hasCheckpoint());
	}

	@Test
	public void setgetPreviousCheckpointTest(@NonStrict final ResourceState mock)
			throws Exception {
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir);

		srt.setPreviousCheckpoint(mock);
		assertTrue(srt.hasPreviousCheckpoint());

	}

	@Test
	public void setgetCheckpointReferenceTestNotNull(
			@NonStrict final ResourceState mock) {
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir);

		srt.setCheckpoint(mock);
		assertEquals(mock, srt.getCheckpointReference());
	}

	@Test
	public void setgetCheckpointReferenceTestNull(
			@NonStrict final ResourceState mock) throws Exception {
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir);

		srt.setCheckpoint(null);
		assertEquals(new NullState(), srt.getCheckpointReference());
	}

	@Test
	public void setgetPrevCheckpointReferenceTestNotNull(
			@NonStrict final ResourceState mock) {
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir);

		srt.setPreviousCheckpoint(mock);
		assertEquals(mock, srt.getPreviousCheckpointReference());
	}

	@Test
	public void setgetPrevCheckpointReferenceTestNull(
			@NonStrict final ResourceState mock) throws Exception {
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir);

		srt.setPreviousCheckpoint(null);
		assertEquals(new NullState(), srt.getPreviousCheckpointReference());
	}

	@Test
	public void updatePreviousCheckpointTest(@NonStrict final ResourceState mock)
			throws Exception {
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir);

		srt.setCheckpoint(mock);
		srt.updatePreviousCheckpoint();
		assertEquals(mock, srt.getCheckpointReference());
	}

	@Test
	public void revertCheckpointTest(@NonStrict final ResourceState mock)
			throws Exception {
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir);

		srt.setPreviousCheckpoint(mock);
		srt.revertCheckpoint();
		assertEquals(mock, srt.getPreviousCheckpointReference());
	}

	@Test
	public void updateCheckpointTestException() throws Exception {

		final InternalResource ir2 = new MockUp<InternalResource>() {
			@Mock
			ResourceState buildState() throws Exception {
				throw new Exception();
			}
		}.getMockInstance();

		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir2);

		try {
			srt.updateCheckpoint();
			fail();
		} catch (ResourceUpdateException e) {
			assertFalse(srt.isConsistent());
		}

	}

	@Test
	public void updateCheckpointTest() throws Exception {
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir);
		new Expectations() {
			{
				ir.buildState();
			}
		};

		srt.updateCheckpoint();
	}

	@Test
	public void rollbackToCheckpointTest(@NonStrict final ResourceState mock)
			throws Exception {
		final StatefulResourceForTesting srt = new StatefulResourceForTesting(
				ir);
		new Expectations() {
			{
				ir.applyState(mock);
			}
		};

		srt.rollbackToCheckpoint();
	}

	public void rollbackToCheckpointTestException() throws Exception {
		final InternalResource ir2 = new MockUp<InternalResource>() {
			@Mock
			void applyState(ResourceState state) throws Exception {
				throw new Exception();
			}
		}.getMockInstance();
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir2);

		try {
			srt.rollbackToCheckpoint();
			fail();
		} catch (ResourceRollbackException e) {
			assertFalse(srt.isConsistent());
		}
	}

	@Test
	public void initializeDecoratorTest() throws Exception {
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir);
		new Expectations() {
			{
				ir.buildState();
			}
		};

		srt.initializeDecorator();
	}

	@Test
	public void initializeDecoratorTestException() {
		final InternalResource ir2 = new MockUp<InternalResource>() {
			@Mock
			ResourceState buildState() throws Exception {
				throw new Exception();
			}
		}.getMockInstance();
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir2);

		try {
			srt.initializeDecorator();
			fail();
		} catch (ResourceInitializeException e) {
		}
	}

	@Test
	public void disposeDecoratorTest() {
		StatefulResourceForTesting srt = new StatefulResourceForTesting(ir);

		srt.disposeDecorator();
		assertEquals(new NullState(), srt.getCheckpointReference());
		assertEquals(new NullState(), srt.getPreviousCheckpointReference());
	}

	public class StatefulResourceForTesting extends StatefulResource {

		public StatefulResourceForTesting(InternalResource resource) {
			super(resource);
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
		public StatefulResource clone() {
			return null;
		}
	}

}
