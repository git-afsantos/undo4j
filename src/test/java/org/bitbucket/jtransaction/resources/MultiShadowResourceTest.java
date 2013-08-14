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

import org.bitbucket.jtransaction.resources.MultiWriterStatefulResource.ThreadLocalStatus;
import org.bitbucket.jtransaction.resources.StatefulResource.Status;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class MultiShadowResourceTest {

	@Injectable
	private InternalResource<?> ir;

	@Tested
	private MultiShadowResource<String> msr;

	@Test
	public void setgetShadowReferenceTest(
			@NonStrict final ResourceState<String> mock) throws Exception {

		msr.setShadow(mock);
		assertEquals(mock, msr.getShadowReference());
	}

	@Test
	public void setShadowTest(@NonStrict final ResourceState<String> mock)
			throws Exception {

		assertFalse(msr.hasShadow());
		msr.setShadow(mock);
		assertTrue(msr.hasShadow());
	}

	@Test
	public void setgetShadowNullTest() throws Exception {

		msr.setShadow(null);
		assertEquals(new NullState<String>(), msr.getShadow());
	}

	@Test
	public void writeTest(@NonStrict final ResourceState<String> mock) {
		msr.write(mock);
		assertEquals(mock, msr.getShadowReference());
	}

	@Test
	public void commitTest() {
		msr.commit();
	}

	@Test
	public void writeCommitTest(@NonStrict final ResourceState<String> mock) {
		final InternalResource<String> ir2 = new MockUp<InternalResource<String>>() {
			@Mock
			boolean isValidState(ResourceState<String> s) {
				return true;
			}
		}.getMockInstance();
		Deencapsulation.setField(msr, "resource", ir2);

		msr.write(mock);
		msr.commit();
		assertFalse(msr.hasShadow());
		assertEquals(Status.COMMITTED,
				((ThreadLocalStatus) Deencapsulation.getField(msr, "status"))
						.get());
	}

	@Test
	public void rollbackTest() {
		msr.rollback();
		assertFalse(msr.hasShadow());
		assertFalse(msr.hasLocalCommit());
		assertTrue(msr.isConsistent());
	}

	@Test
	public void disposeDecoratorTest() {
		msr.disposeDecorator();
		assertFalse(msr.hasShadow());
	}
}
