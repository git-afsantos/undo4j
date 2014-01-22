package com.github.undo4j.list;

import com.github.undo4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * ListTest
 * 
 * @author afs
 * @version 2013
 */

public class ListTest {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class ListTest.
     */
    private ListTest() { throw new AssertionError(); }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static void testAdd() {
        // -- Set up ----------------------------------------------------------
        TransactionManager man = TransactionManagers.newSingleThreadManager();
        ResourceReference<List<Integer>> list =
            man.register((List<Integer>) new ArrayList<Integer>());
        list.get().add(1);
        list.get().add(2);
        list.get().add(3);
        try {
            man.execute(new Adder(list, 4));
            man.execute(new Adder(list, 5));
            man.execute(new Adder(list, 6));
        } catch (TransactionExecutionException ex) {
            ex.printStackTrace();
        } finally {
            man.shutdown();
            System.out.println(list.get().toString());
        }
    }


    /** */
    public static void testRemove() {
        // -- Set up ----------------------------------------------------------
        TransactionManager man = TransactionManagers.newSingleThreadManager();
        ResourceReference<List<Integer>> list =
            man.register((List<Integer>) new ArrayList<Integer>());
        list.get().add(1);
        list.get().add(2);
        list.get().add(1);
        try {
            man.execute(new Remover(list, 4));
            man.execute(new Remover(list, 1));
            man.execute(new Remover(list, 1));
        } catch (TransactionExecutionException ex) {
            ex.printStackTrace();
        } finally {
            man.shutdown();
            System.out.println(list.get().toString());
        }
    }


    /** */
    public static void testAddRollback() {
        // -- Set up ----------------------------------------------------------
        TransactionManager man = TransactionManagers.newSingleThreadManager();
        ResourceReference<List<Integer>> list =
            man.register((List<Integer>) new ArrayList<Integer>());
        list.get().add(1);
        list.get().add(2);
        list.get().add(3);
        try {
            man.execute(new AddFailure(list));
        } catch (TransactionExecutionException ex) {
            ex.printStackTrace();
        } finally {
            man.shutdown();
            System.out.println(list.get().toString());
        }
    }


    /** */
    public static void testConcurrentAdd() {
        // -- Set up ----------------------------------------------------------
        ConcurrentTransactionManager man =
            TransactionManagers.newFixedThreadPool(2);
        ResourceReference<List<Integer>> list =
            man.register((List<Integer>) new ArrayList<Integer>());
        list.get().add(1);
        list.get().add(2);
        list.get().add(3);
        try {
            List<Future<Void>> fs = new ArrayList<>();
            for (int i = 4; i < 7; ++i) {
                fs.add(man.submit(new Adder(list, i)));
            }
            for (Future<Void> f: fs) { f.get(); }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (ExecutionException ex) {
            // Should have a TransactionExecutionException as cause.
            if (ex.getCause() instanceof TransactionExecutionException) {
                ((TransactionExecutionException) ex.getCause())
                    .printStackTrace();
            } else { ex.printStackTrace(); }
        } finally {
            man.shutdown();
            System.out.println(list.get().toString());
        }
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /** */
    private static class Adder implements ClientRunnable {
        final ResourceReference<List<Integer>> list;
        final Integer element;

        Adder(ResourceReference<List<Integer>> list, Integer element) {
            this.list = list;
            this.element = element;
        }

        @Override
        public void run(OperationDispatcher dispatcher) {
            dispatcher.write(ListOperations.add(list, element));
        }
    }


    /** */
    private static class Remover implements ClientRunnable {
        final ResourceReference<List<Integer>> list;
        final Integer element;

        Remover(ResourceReference<List<Integer>> list, Integer element) {
            this.list = list;
            this.element = element;
        }

        @Override
        public void run(OperationDispatcher dispatcher) {
            dispatcher.write(ListOperations.remove(list, element));
        }
    }


    /** */
    private static class AddFailure implements ClientRunnable {
        final ResourceReference<List<Integer>> list;

        AddFailure(ResourceReference<List<Integer>> list) {
            this.list = list;
        }

        @Override
        public void run(OperationDispatcher dispatcher) {
            dispatcher.write(ListOperations.add(list, 1));
            dispatcher.write(ListOperations.add(list, 2));
            dispatcher.write(ListOperations.add(list, 3));
            throw new RuntimeException("List failure");
        }
    }
}
