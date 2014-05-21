package com.github.undo4j.counter;

import com.github.undo4j.*;

import java.util.Arrays;

/**
 * CounterOperations
 * 
 * @author afs
 * @version 2013
 */

public class CounterOperations {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class CounterOperations.
     */
    private CounterOperations() { throw new AssertionError(); }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static WriteOperation increment
            (ResourceReference<Counter> counter) {
        return new Increment(counter);
    }

    /** */
    public static ReadOperation<Integer> read
            (ResourceReference<Counter> counter) {
        return new Read(counter);
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /** */
    private static class Increment implements WriteOperation {
        private final Counter counter;
        private final ResourceId id;

        Increment(ResourceReference<Counter> counter) {
            this.counter = counter.get();
            this.id = counter.id();
        }

        @Override
        public void write() {
            counter.increment();
        }

        @Override
        public void undo() {
            counter.decrement();
        }

        @Override
        public Iterable<ResourceId> resources() {
            return Arrays.asList(id);
        }
    }


    /** */
    private static class Read implements ReadOperation<Integer> {
        private final Counter counter;
        private final ResourceId id;

        Read(ResourceReference<Counter> counter) {
            this.counter = counter.get();
            this.id = counter.id();
        }

        @Override
        public Integer read() {
            return counter.get();
        }

        @Override
        public Iterable<ResourceId> resources() {
            return Arrays.asList(id);
        }
    }
}
