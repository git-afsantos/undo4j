/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Andre Santos, Victor Miraldo
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.bitbucket.jtransaction.transactions;

import static org.bitbucket.jtransaction.common.Check.checkArgument;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bitbucket.jtransaction.common.Copyable;

/**
 * TransactionStatistics
 * 
 * @author afs
 * @version 2013
*/

public final class TransactionStatistics implements Copyable<TransactionStatistics> {
    // instance variables
    private int requested, reads, writes;
    private final Map<String, ResourceStatistics> resources;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class TransactionStatistics. */
    TransactionStatistics() {
        this(0, 0, 0, new HashMap<String, ResourceStatistics>());
    }

    /** Parameter constructor of objects of class TransactionStatistics. */
    TransactionStatistics(int req, int rd, int wr) {
        this(req, rd, wr, new HashMap<String, ResourceStatistics>());
    }

    /** Parameter constructor of objects of class TransactionStatistics. */
    TransactionStatistics(int req, int rd, int wr, Map<String, ResourceStatistics> map) {
        checkArgument(req >= 0);
        checkArgument(rd >= 0);
        checkArgument(wr >= 0);
        checkArgument(map);
        // ----------------------------- //
        requested = req;
        reads = rd;
        writes = wr;
        resources = map;
    }

    /** Copy constructor of objects of class TransactionStatistics. */
    private TransactionStatistics(TransactionStatistics instance) {
        this(instance.getRequestedCount(), instance.getReadCount(), instance.getWriteCount(), instance
            .getResourceStatistics());
    }

    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    public int getRequestedCount() {
        return requested;
    }

    /** */
    public int getUsedCount() {
        return resources.size();
    }

    /** */
    public int getReadCount() {
        return reads;
    }

    /** */
    public int getWriteCount() {
        return writes;
    }

    /** */
    public ResourceStatistics getStatisticsFor(String id) {
        return resources.containsKey(id) ? resources.get(id).clone() : new ResourceStatistics();
    }

    /** */
    public Map<String, ResourceStatistics> getResourceStatistics() {
        Map<String, ResourceStatistics> copy = new HashMap<String, ResourceStatistics>(resources.size());
        Set<String> keys = resources.keySet();
        for (String k : keys) {
            copy.put(k, resources.get(k).clone());
        }
        return copy;
    }

    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    public boolean hasRequestedResources() {
        return requested > 0;
    }

    /** */
    public boolean hasUsedResources() {
        return resources.size() > 0;
    }

    /** */
    public boolean hasReads() {
        return reads > 0;
    }

    /** */
    public boolean hasReads(String id) {
        return resources.get(id).hasReads();
    }

    /** */
    public boolean hasWrites() {
        return writes > 0;
    }

    /** */
    public boolean hasWrites(String id) {
        return resources.get(id).hasWrites();
    }

    /** */
    public boolean hasOperations() {
        return reads > 0 || writes > 0;
    }

    /** */
    public boolean contains(String resource) {
        return resources.containsKey(resource);
    }

    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    void put(String resource) {
        resources.put(resource, new ResourceStatistics());
    }

    /** */
    void put(String resource, ResourceStatistics rs) {
        resources.put(resource, rs);
        reads += rs.getReadCount();
        writes += rs.getWriteCount();
    }

    /** */
    void putIfAbsent(String resource) {
        if (!resources.containsKey(resource)) {
            this.put(resource);
        }
    }

    /** */
    void putIfAbsent(String resource, ResourceStatistics rs) {
        if (!resources.containsKey(resource)) {
            this.put(resource, rs);
        }
    }

    /** */
    void incrementRequestedCount() {
        ++requested;
    }

    /** */
    void incrementRequestedCount(int i) {
        requested += i;
    }

    /** */
    void incrementReadCount(String resource) {
        incrementReadCount(resource, 1);
    }

    /** */
    void incrementReadCount(String resource, int i) {
        resources.get(resource).incrementReadCount(i);
        reads += i;
    }

    /** */
    void incrementWriteCount(String resource) {
        incrementWriteCount(resource, 1);
    }

    /** */
    void incrementWriteCount(String resource, int i) {
        resources.get(resource).incrementWriteCount(i);
        writes += i;
    }

    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Equivalence relation. Contract (for any non-null reference values
            resources, y, and z):
        * Refleresourcesive: resources.equals(resources).
        * Symmetric: resources.equals(y) iff y.equals(resources).
        * Transitive: if resources.equals(y) and y.equals(z), then resources.equals(z).
        * Consistency: successive calls (with no modification of the equality
            fields) return the same result.
        * resources.equals(null) should return false.
    */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || this.getClass() != o.getClass())
            return false;
        TransactionStatistics n = (TransactionStatistics)o;
        return (requested == n.getRequestedCount() && reads == n.getReadCount() && writes == n.getWriteCount() && resources
            .equals(n.getResourceStatistics()));
    }

    /** Contract:
        * Consistency: successive calls (with no modification of the equality
            fields) return the same code.
        * Function: two equal objects have the same (unique) hash code.
        * (Optional) Injection: unequal objects have different hash codes.
    * Common practices:
        * boolean: calculate (f ? 0 : 1);
        * byte, char, short or int: calculate (int) f;
        * long: calculate (int) (f ^ (f >>> 32));
        * float: calculate Float.floatToIntBits(f);
        * double: calculate Double.doubleToLongBits(f)
            and handle the return value like every long value;
        * Object: use (f == null ? 0 : f.hashCode());
        * Array: recursion and combine the values.
    * Formula:
        hash = prime * hash + codeForField
    */
    @Override
    public int hashCode() {
        int hash = 37 + requested;
        hash = 37 * hash + reads;
        hash = 37 * hash + writes;
        hash = 37 * hash + resources.hashCode();
        return hash;
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("#TRANSACTION_STATISTICS $ ");
        sb.append(requested);
        sb.append(" $ ");
        sb.append(reads);
        sb.append(" $ ");
        sb.append(writes);
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public TransactionStatistics clone() {
        return new TransactionStatistics(this);
    }
}
