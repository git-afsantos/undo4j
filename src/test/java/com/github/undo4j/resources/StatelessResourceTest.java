package com.github.undo4j.resources;

import static org.junit.Assert.fail;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;

import org.junit.Before;
import org.junit.Test;

import com.github.undo4j.common.LockManager;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.ResourceReadException;
import com.github.undo4j.resources.ResourceState;
import com.github.undo4j.resources.ResourceWriteException;
import com.github.undo4j.resources.StatelessResource;

public class StatelessResourceTest {

	@Injectable
	private InternalResource<String> ir;

	@Injectable
	private LockManager lockManager;

	@Injectable
	private ResourceState<String> stringResource;

	private StatelessResource<String> srt;

	@Before
	public void setup() {
		srt = new StatelessResource<String>(ir, lockManager);
	}

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
	public void writeTest() throws Exception {
		new Expectations() {
			{
				ir.applyState(stringResource);
				minTimes = 1;
			}
		};

		srt.write(stringResource);
	}

	@Test
	public void writeTestException() {
		Deencapsulation.setField(srt, "resource", irEx);
		try {
			srt.write(stringResource);
			fail();
		} catch (ResourceWriteException e) {
		}
	}
}
