package org.bitbucket.jtransaction.transactions;

import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.*;
import org.bitbucket.jtransaction.common.*;
import org.bitbucket.jtransaction.resources.*;

public final class RunTransactionTest {
	
	
	@Test
	public void testNormalTransaction()
			throws InterruptedException, ExecutionException {
		
		
		ManagedResource<String> mr = ManagedResource.from(
			new ShadowResource<String>(
				new StringResource(),
				LockManagers.newNullLockManager()
			)
		);
		TransactionManager tm = TransactionManagers.newSynchronousManager();
		Future<String> f = tm.submit(new WriteTransaction("Hello", mr));
		String s = f.get();
		
		
		Assert.assertEquals("Hello", s);
	}


	@Test
	public void testResourceHidingEmptyTransaction()
			throws InterruptedException, ExecutionException {
		
		
		ManagedResource<String> mr = ManagedResource.from(
			new ShadowResource<String>(
				new StringResource(),
				LockManagers.newNullLockManager()
			)
		);
		
		try {
			TransactionManager tm = TransactionManagers.newSynchronousManager();
			Future<String> f = tm.submit(new ResourceHidingEmptyTransaction("Hello", mr));
			String s = f.get();
			fail();
			System.out.println(s);
		} catch (ExecutionException e) {}
	}
	
	
	@Test
	public void testResourceHidingTransaction() throws InterruptedException {
		
		
		ManagedResource<String> mr = ManagedResource.from(
			new ShadowResource<String>(
				new StringResource(),
				LockManagers.newNullLockManager()
			)
		);
		ManagedResource<String> mrHidden = ManagedResource.from(
			new ShadowResource<String>(
				new StringResource(),
				LockManagers.newNullLockManager()
			)
		);
		TransactionManager tm = TransactionManagers.newSynchronousManager();
		Future<String> f = tm.submit(new ResourceHidingTransaction("Hello", mr, mrHidden));
		
		try {
			String s = f.get();
			fail();
			System.out.println(s);
		}
		catch (ExecutionException ex) {}
	}
}
