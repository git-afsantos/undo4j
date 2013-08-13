package org.bitbucket.jtransaction.resources;

import static org.junit.Assert.fail;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.NonStrict;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class StatelessResourceTest {

	private InternalResource ir = new MockUp<InternalResource>() {
	}.getMockInstance();

	private final InternalResource irEx = new MockUp<InternalResource>() {
		@Mock
		ResourceState buildState() throws Exception {
			throw new Exception();
		}

		@Mock
		void applyState(ResourceState state) throws Exception {
			throw new Exception();
		}
	}.getMockInstance();

	@Test
	public void readTest() throws Exception {
		StatelessResourceForTesting srt = new StatelessResourceForTesting(ir);
		new Expectations() {
			{
				ir.buildState();
			}
		};

		srt.read();
	}

	@Test
	public void readTestException() {
		StatelessResourceForTesting srt = new StatelessResourceForTesting(irEx);

		try {
			srt.read();
			fail();
		} catch (ResourceReadException e) {
		}
	}

	@Test
	public void writeTest(@NonStrict final ResourceState mock) throws Exception {
		StatelessResourceForTesting srt = new StatelessResourceForTesting(ir);
		new Expectations() {
			{
				ir.applyState(mock);
			}
		};

		srt.write(mock);
	}

	@Test
	public void writeTestException(@NonStrict final ResourceState mock) {
		StatelessResourceForTesting srt = new StatelessResourceForTesting(irEx);

		try {
			srt.write(mock);
			fail();
		} catch (ResourceWriteException e) {
		}
	}

	public class StatelessResourceForTesting extends StatelessResource {

		public StatelessResourceForTesting(InternalResource resource) {
			super(resource);
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
		public AbstractResource clone() {
			return null;
		}
	}
}
