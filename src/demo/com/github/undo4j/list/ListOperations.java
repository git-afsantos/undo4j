package com.github.undo4j.list;

import com.github.undo4j.*;

import java.util.Arrays;
import java.util.List;

/**
 * ListOperations
 * 
 * @author afs
 * @version 2013
 */

public final class ListOperations {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Empty constructor of objects of class ListOperations.
     */
    private ListOperations() { throw new AssertionError(); }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static <T> WriteOperation add
            (ResourceReference<List<T>> list, T element) {
        return new AddOperation<T>(list, element);
    }

    /** */
    public static <T> WriteOperation remove
            (ResourceReference<List<T>> list, T element) {
        return new RemoveOperation<T>(list, element);
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /**
     * Adds an element to a list.
     * The undo operation is tricky.
     * If we just used {@code list.remove(element)},
     * it could remove the wrong occurrence of an element.
     * This is due to lists allowing repeated elements,
     * and {@code remove(element)} removing the first occurrence.
     * 
     * Example:
     *  list = ['a', 'b', 'c'];
     * write:
     *  list.add('a');
     * undo:
     *  list.remove('a');
     * result on rollback:
     *  list = ['b', 'c', 'a'];
     */
    private static class AddOperation<T> implements WriteOperation {
        final List<T> list;
        final ResourceId id;
        final T element;
        private int index = 0;
        private boolean done = false;

        AddOperation(ResourceReference<List<T>> list, T element) {
            this.list       = list.get();
            this.id         = list.id();
            this.element    = element;
        }

        @Override
        public void write() {
            index = list.size();
            list.add(element);
            done = true;
        }

        @Override
        public void undo() {
            if (done) { list.remove(index); }
        }

        @Override
        public Iterable<ResourceId> resources() {
            return Arrays.asList(id);
        }
    }


    /**
     * Removes an element from a list.
     * The undo operation is tricky.
     * If we just used {@code list.add(element)},
     * it could add the element at the wrong index.
     * This is mostly due to {@code remove(element)}
     * removing the first occurrence, and
     * {@code add(element)} appending to the list.
     * 
     * Example:
     *  list = ['a', 'b', 'a'];
     * write:
     *  list.remove('a');
     * undo:
     *  list.add('a');
     * result on rollback:
     *  list = ['b', 'a', 'a'];
     */
    private static class RemoveOperation<T> implements WriteOperation {
        final List<T> list;
        final ResourceId id;
        final T element;
        private int index = 0;
        private boolean done = false;

        RemoveOperation(ResourceReference<List<T>> list, T element) {
            this.list       = list.get();
            this.id         = list.id();
            this.element    = element;
        }

        @Override
        public void write() {
            index = list.indexOf(element);
            done = list.remove(element);
        }

        @Override
        public void undo() {
            if (done) { list.add(index, element); }
        }

        @Override
        public Iterable<ResourceId> resources() {
            return Arrays.asList(id);
        }
    }
}
