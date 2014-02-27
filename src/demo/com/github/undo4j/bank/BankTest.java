package com.github.undo4j.bank;

import com.github.undo4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * BankTest
 * 
 * @author afs
 * @version 2013
 */

public class BankTest {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class BankTest.
     */
    private BankTest() { throw new AssertionError(); }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static void testTransfer() {
        TransactionManager man = TransactionManagers.newSingleThreadManager();
        ResourceReference<Account> a1 = man.register(Account.newInstance(100));
        ResourceReference<Account> a2 = man.register(Account.newInstance(100));

        System.out.println("-- Test Transfer:");
        System.out.println(a1.get().toString());
        System.out.println(a2.get().toString());
        try {
            man.execute(new Transfer(a1, a2, 100));
        } catch (TransactionExecutionException ex) {
            ex.printStackTrace();
        } finally {
            man.shutdown();
            System.out.println("-- Result:");
            System.out.println(a1.get().toString());
            System.out.println(a2.get().toString());
        }
    }


    /** */
    public static void testBadTransfer() {
        TransactionManager man = TransactionManagers.newSingleThreadManager();
        ResourceReference<Account> a1 = man.register(Account.newInstance(50));
        ResourceReference<Account> a2 = man.register(Account.newInstance(50));

        System.out.println("-- Test Bad Transfer:");
        System.out.println(a1.get().toString());
        System.out.println(a2.get().toString());
        try {
            man.execute(new Transfer(a1, a2, 100));
        } catch (TransactionExecutionException ex) {
            ex.printStackTrace();
        } finally {
            man.shutdown();
            System.out.println("-- Result:");
            System.out.println(a1.get().toString());
            System.out.println(a2.get().toString());
        }
    }


    /** */
    public static void testConcurrentTransfer() {
        ConcurrentTransactionManager man =
            TransactionManagers.newFixedThreadPool(2);
        ResourceReference<Account> a1 = man.register(Account.newInstance(100));
        ResourceReference<Account> a2 = man.register(Account.newInstance(100));

        System.out.println("-- Test Concurrent Transfer:");
        System.out.println(a1.get().toString());
        System.out.println(a2.get().toString());
        try {
            List<Future<Void>> fs = new ArrayList<>(100);
            for (int i = 0; i < 100; ++i) {
                fs.add(man.submit(new Transfer(a1, a2, 1)));
            }
            for (Future<Void> f: fs) { f.get(); }
        } catch (InterruptedException ex) {
            // Shouldn't happen.
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            // Should have a TransactionExecutionException as cause.
            if (ex.getCause() instanceof TransactionExecutionException) {
                ((TransactionExecutionException) ex.getCause())
                    .printStackTrace();
            } else { ex.printStackTrace(); }
        } finally {
            man.shutdown();
            System.out.println("-- Result:");
            System.out.println(a1.get().toString());
            System.out.println(a2.get().toString());
        }
    }


    /** */
    public static void testBadConcurrentTransfer() {
        ConcurrentTransactionManager man =
            TransactionManagers.newFixedThreadPool(2);
        ResourceReference<Account> a1 = man.register(Account.newInstance(100));
        ResourceReference<Account> a2 = man.register(Account.newInstance(100));

        System.out.println("-- Test Bad Concurrent Transfer:");
        System.out.println(a1.get().toString());
        System.out.println(a2.get().toString());
        try {
            List<Future<Void>> fs = new ArrayList<>(101);
            for (int i = 0; i < 101; ++i) {
                fs.add(man.submit(new Transfer(a1, a2, 1)));
            }
            for (Future<Void> f: fs) { f.get(); }
        } catch (InterruptedException ex) {
            // Shouldn't happen.
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            // Should have a TransactionExecutionException as cause.
            if (ex.getCause() instanceof TransactionExecutionException) {
                ((TransactionExecutionException) ex.getCause())
                    .printStackTrace();
            } else { ex.printStackTrace(); }
        } finally {
            man.shutdown();
            System.out.println(a1.get().toString());
            System.out.println(a2.get().toString());
        }
    }


    /** */
    public static void testDeadlock() {
        ConcurrentTransactionManager man =
            TransactionManagers.newFixedThreadPool(2);
        ResourceReference<Account> a1 = man.register(Account.newInstance(100));
        ResourceReference<Account> a2 = man.register(Account.newInstance(100));
        CyclicBarrier barrier = new CyclicBarrier(2);

        System.out.println("-- Test Deadlock Transfer:");
        System.out.println(a1.get().toString());
        System.out.println(a2.get().toString());
        try {
            Future<Void> f1 = man.submit(new SyncTransfer(a1, a2, 50, barrier));
            Future<Void> f2 = man.submit(new SyncTransfer(a2, a1, 25, barrier));
            f1.get();
            f2.get();
        } catch (InterruptedException ex) {
            // Shouldn't happen.
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            // Should have a TransactionExecutionException as cause.
            if (ex.getCause() instanceof TransactionExecutionException) {
                ((TransactionExecutionException) ex.getCause())
                    .printStackTrace();
            } else { ex.printStackTrace(); }
        } finally {
            man.shutdown();
            System.out.println(a1.get().toString());
            System.out.println(a2.get().toString());
        }
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /** */
    private static class Transfer implements ClientRunnable {
        final ResourceReference<Account> source, target;
        final int times;

        Transfer(ResourceReference<Account> a1,
                ResourceReference<Account> a2,
                int times) {
            this.source = a1;
            this.target = a2;
            this.times = times;
        }

        @Override
        public void run(OperationDispatcher dispatcher) {
            for (int i = 0; i < times; ++i) {
                dispatcher.write
                    (AccountOperations.transfer(source, target, 1));
            }
        }
    }


    /** */
    private static class SyncTransfer implements ClientRunnable {
        final ResourceReference<Account> source, target;
        final int times;
        final CyclicBarrier barrier;

        SyncTransfer(ResourceReference<Account> a1,
                ResourceReference<Account> a2,
                int times,
                CyclicBarrier barrier) {
            this.source = a1;
            this.target = a2;
            this.times = times;
            this.barrier = barrier;
        }

        @Override
        public void run(OperationDispatcher dispatcher) {
            try {
                dispatcher.write(AccountOperations.withdraw(source, times));
                barrier.await();
                dispatcher.write(AccountOperations.deposit(target, times));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
