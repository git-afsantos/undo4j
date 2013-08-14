package org.bitbucket.jtransaction.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.NonStrict;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.bitbucket.jtransaction.resources.StatefulResource.Status;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class SingleWriterStatefulResourceTest {
	@Injectable
	private InternalResource<String> resource;
	@Tested
	private SingleWriterStatefulResource<String> swsr;

	@Test
	public void setgetLocalCommitTest(
			@NonStrict final ResourceState<String> mock) throws Exception {
		swsr.setLocalCommit(mock);
		assertTrue(swsr.hasLocalCommit());
	}

	@Test
	public void setgetLocalCommitReferenceTest(
			@NonStrict final ResourceState<String> mock) throws Exception {
		swsr.setLocalCommit(mock);
		assertEquals(mock, swsr.getLocalCommitReference());
	}

	@Test
	public void setgetStatusTest(@NonStrict final Status mock) {
		swsr.setStatus(mock);
		assertEquals(mock, swsr.getStatus());
	}

	@Test
	public void writeUpdateTest(@NonStrict final ResourceState<String> mock)
			throws Exception {
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

	@Test
	public void disposeDecoratorTest() throws Exception {
		swsr.disposeDecorator();
		assertFalse(swsr.hasLocalCommit());
	}

}
