package com.github.undo4j.counter;

import com.github.undo4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * CounterTest
 * 
 * @author afs
 * @version 2013
 */

public class CounterTest {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class CounterTest.
     */
    private CounterTest() { throw new AssertionError(); }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static void test() {
        Counter counter = new Counter();
        TransactionManager man = TransactionManagers.newSingleThreadManager();
        ResourceReference<Counter> ref = man.register(counter);
        try {
            System.out.println(man.execute(new Incrementor(ref)));
        } catch (TransactionExecutionException ex) {
            ex.printStackTrace();
        } finally {
            man.shutdown();
        }
    }

    /** */
    public static void testFail() {
        Counter counter = new Counter();
        TransactionManager man = TransactionManagers.newSingleThreadManager();
        ResourceReference<Counter> ref = man.register(counter);
        try {
            System.out.println(man.execute(new BadIncrementor(ref)));
        } catch (TransactionExecutionException ex) {
            ex.printStackTrace();
            System.out.println(counter);
        } finally {
            man.shutdown();
        }
    }


    /** */
    public static void testConcurrent() {
        Counter counter = new Counter();
        ConcurrentTransactionManager man =
            TransactionManagers.newFixedThreadPool(4);
        ResourceReference<Counter> ref = man.register(counter);
        try {
            List<Future<Integer>> fs = new ArrayList<>(100);
            for (int i = 0; i < 100; ++i) {
                fs.add(man.submit(new Incrementor(ref)));
            }
            int m = 0;
            for (Future<Integer> f: fs) {
                m = Math.max(m, f.get().intValue());
            }
            System.out.println(m);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            man.shutdown();
        }
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /** */
    private static class Incrementor implements ClientCallable<Integer> {
        private final ResourceReference<Counter> counter;

        Incrementor(ResourceReference<Counter> counter) {
            this.counter = counter;
        }

        @Override
        public Integer call(OperationDispatcher dispatcher) {
            dispatcher.write(CounterOperations.increment(counter));
            return dispatcher.read(CounterOperations.read(counter));
        }
    }

    /** */
    private static class BadIncrementor implements ClientCallable<Integer> {
        private final ResourceReference<Counter> counter;

        BadIncrementor(ResourceReference<Counter> counter) {
            this.counter = counter;
        }

        @Override
        public Integer call(OperationDispatcher dispatcher) {
            dispatcher.write(CounterOperations.increment(counter));
            throw new RuntimeException("Counter rollback requested");
        }
    }
}
