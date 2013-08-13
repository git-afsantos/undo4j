package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.Copyable;


/**
 * ResourceStatistics
 * 
 * @author afs
 * @version 2013
*/

public final class ResourceStatistics implements Copyable<ResourceStatistics> {
    // instance variables
    private int reads, writes;
    private long lastRead, lastWrite;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class ResourceStatistics. */
    ResourceStatistics() { this(0, 0, 0, 0); }


    /** Parameter constructor of objects of class ResourceStatistics. */
    ResourceStatistics(int r, int w) {
        this(r, w, 0, 0);
    }


    /** Parameter constructor of objects of class ResourceStatistics. */
    ResourceStatistics(int r, int w, long rTime, long wTime) {
        reads       = r;
        writes      = w;
        lastRead    = rTime;
        lastWrite   = wTime;
    }


    /** Copy constructor of objects of class ResourceStatistics. */
    private ResourceStatistics(ResourceStatistics instance) {
        this(
            instance.getReadCount(),
            instance.getWriteCount(),
            instance.getLastReadTime(),
            instance.getLastWriteTime()
        );
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    public int getReadCount() { return reads; }

    /** */
    public int getWriteCount() { return writes; }

    /** */
    long getLastReadTime() { return lastRead; }

    /** */
    long getLastWriteTime() { return lastWrite; }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    void setReadCount(int r) { reads = r; }

    /** */
    void resetReadCount() { reads = 0; }

    /** */
    void setWriteCount(int w) { writes = w; }

    /** */
    void resetWriteCount() { writes = 0; }

    /** */
    void setLastReadTime(long t) { lastRead = t; }

    /** */
    void setLastReadTime() { lastRead = System.currentTimeMillis(); }

    /** */
    void setLastWriteTime(long t) { lastWrite = t; }

    /** */
    void setLastWriteTime() { lastWrite = System.currentTimeMillis(); }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    public boolean isAccessed() { return hasReads() || hasWrites(); }


    /** */
    public boolean hasReads() { return reads > 0; }


    /** */
    public boolean hasWrites() { return writes > 0; }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** Sums two statistics into one.
     * The resulting object has the sum of the counter values.
     * Returns the non-null object if one of the arguments is null.
    */
    public static ResourceStatistics sum(
            ResourceStatistics s1, ResourceStatistics s2
    ) {
        // If both are null, return empty statistics.
        if (s1 == null && s2 == null) { return new ResourceStatistics(); }
        // If the first is null, return the second.
        if (s1 == null) { return s2; }
        // If the second is null, return the first.
        if (s2 == null) { return s1; }
        // If none is null, combine and return new statistics.
        return new ResourceStatistics(
            s1.getReadCount()           +   s2.getReadCount(),
            s1.getWriteCount()          +   s2.getWriteCount(),
            Math.max(s1.getLastReadTime(),  s2.getLastReadTime()),
            Math.max(s1.getLastWriteTime(), s2.getLastWriteTime())
        );
    }


    /** */
    public void add(ResourceStatistics rs) {
        if (rs == null) { return; }
        reads += rs.getReadCount();
        writes += rs.getWriteCount();
        lastRead = Math.max(lastRead, rs.getLastReadTime());
        lastWrite = Math.max(lastWrite, rs.getLastWriteTime());
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    void incrementReadCount() { ++reads; }

    /** */
    void incrementWriteCount() { ++writes; }

    /** */
    void incrementReadCount(int i) { reads += i; }

    /** */
    void incrementWriteCount(int i) { writes += i; }



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Equivalence relation. Contract (for any non-null reference values
            reads, y, and z):
        * Reflereadsive: reads.equals(reads).
        * Symmetric: reads.equals(y) iff y.equals(reads).
        * Transitive: if reads.equals(y) and y.equals(z), then reads.equals(z).
        * Consistency: successive calls (with no modification of the equality
            fields) return the same result.
        * reads.equals(null) should return false.
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ResourceStatistics n = (ResourceStatistics) o;
        return (reads == n.getReadCount() &&
                writes == n.getWriteCount());
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
    public int hashCode() { return 37 * writes + reads; }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("#RESOURCE_STATISTICS $ ");
        sb.append(reads);
        sb.append(" $ ");
        sb.append(writes);
        sb.append(" $ ");
        sb.append(lastRead);
        sb.append(" $ ");
        sb.append(lastWrite);
        return sb.toString();
    }

    /** Returns a JSON string representation of the object. */
    public String toJsonString() {
        StringBuilder sb = new StringBuilder("{\"reads\":");
        sb.append(reads);
        sb.append(",\"writes\":");
        sb.append(writes);
        sb.append(",\"lastRead\":");
        sb.append(lastRead);
        sb.append(",\"lastWrite\":");
        sb.append(lastWrite);
        sb.append('}');
        return sb.toString();
    }

    /** Returns a XML string representation of the object. */
    public String toXmlString() {
        StringBuilder sb = new StringBuilder("<ResourceStatistics reads=\"");
        sb.append(reads);
        sb.append("\" writes=\"");
        sb.append(writes);
        sb.append("\" lastRead=\"");
        sb.append(lastRead);
        sb.append("\" lastWrite=\"");
        sb.append(lastWrite);
        sb.append("\"></ResourceStatistics>");
        return sb.toString();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public ResourceStatistics clone() { return new ResourceStatistics(this); }
}
