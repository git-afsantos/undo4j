package org.bitbucket.jtransaction.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mockit.Deencapsulation;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.NonStrict;
import mockit.Tested;
import mockit.integration.junit4.JMockit;

import org.bitbucket.jtransaction.resources.Resource.Status;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class SingleWriterStatefulResourceTest {
	@Injectable
	private InternalResource resource;
	@Tested
	private SingleWriterStatefulResource swsr;

	@Test
	public void setgetLocalCommitTest(@NonStrict final ResourceState mock)
			throws Exception {
		swsr.setLocalCommit(mock);
		assertTrue(swsr.hasLocalCommit());
	}

	@Test
	public void setgetLocalCommitReferenceTest(
			@NonStrict final ResourceState mock) throws Exception {
		swsr.setLocalCommit(mock);
		assertEquals(mock, swsr.getLocalCommitReference());
	}

	@Test
	public void setgetStatusTest(@NonStrict final Status mock) {
		swsr.setStatus(mock);
		assertEquals(mock, swsr.getStatus());
	}

	@Test
	public void writeUpdateTest(@NonStrict final ResourceState mock)
			throws Exception {
		final InternalResource ir2 = new MockUp<InternalResource>() {
			@Mock
			boolean isValidState(ResourceState s) {
				return true;
			}
		}.getMockInstance();
		Deencapsulation.setField(swsr, "resource", ir2);
		swsr.write(mock);
		swsr.commit();
		swsr.update();
		assertEquals(Status.UPDATED, swsr.getStatus());
	}

	@Test
	public void disposeDecoratorTest() throws Exception {
		swsr.disposeDecorator();
		assertFalse(swsr.hasLocalCommit());
	}

}
