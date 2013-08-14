package org.bitbucket.jtransaction.resources;

import static org.junit.Assert.fail;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.NonStrict;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class StatelessResourceTest {
	@Injectable
	private InternalResource<String> ir;

	@Tested
	private StatelessResource<String> srt;

	private final InternalResource<String> irEx = new MockUp<InternalResource<String>>() {
		@Mock
		ResourceState<String> buildState() throws Exception {
			throw new Exception();
		}

		@Mock
		void applyState(ResourceState<String> state) throws Exception {
			throw new Exception();
		}
	}.getMockInstance();

	@Test
	public void readTest() throws Exception {
		new Expectations() {
			{
				ir.buildState();
			}
		};

		srt.read();
	}

	@Test
	public void readTestException() {
		Deencapsulation.setField(srt, "resource", irEx);
		try {
			srt.read();
			fail();
		} catch (ResourceReadException e) {
		}
	}

	@Test
	public void writeTest(@NonStrict final ResourceState<String> mock)
			throws Exception {
		new Expectations() {
			{
				ir.applyState(mock);
			}
		};

		srt.write(mock);
	}

	@Test
	public void writeTestException(@NonStrict final ResourceState<String> mock) {
		Deencapsulation.setField(srt, "resource", irEx);
		try {
			srt.write(mock);
			fail();
		} catch (ResourceWriteException e) {
		}
	}
}
