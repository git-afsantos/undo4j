package com.github.undo4j;


/**
 * ResourceId
 * 
 * @author afs
 * @version 2013
 */

public final class ResourceId implements Comparable<ResourceId> {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** Unique identifier. */
    private final int id;

    /** Common prefix to a group of identifiers. */
    private final int prefix;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class ResourceId.
     */
    ResourceId(final int prefix, final int id) {
        this.id     = id;
        this.prefix = prefix;
    }



    /*************************************************************************\
     *  Equals, HashCode, ToString & CompareTo
    \*************************************************************************/

    /**
     *  Equivalence relation.
     *  Contract (for any non-null reference values x, y, and z):
     *      Reflexive: x.equals(x).
     *      Symmetric: x.equals(y) iff y.equals(x).
     *      Transitive: if x.equals(y) and y.equals(z), then x.equals(z).
     *      Consistency: successive calls return the same result,
     *          assuming no modification of the equality fields.
     *      x.equals(null) should return false.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ResourceId)) { return false; }
        ResourceId n = (ResourceId) o;
        return id == n.id && prefix == n.prefix;
    }

    /**
     *  Contract:
     *      Consistency: successive calls return the same code,
     *          assuming no modification of the equality fields.
     *      Function: two equal objects have the same (unique) hash code.
     *      (Optional) Injection: unequal objects have different hash codes.
     *
     *  Common practices:
     *      boolean: calculate (f ? 0 : 1);
     *      byte, char, short or int: calculate (int) f;
     *      long: calculate (int) (f ^ (f >>> 32));
     *      float: calculate Float.floatToIntBits(f);
     *      double: calculate Double.doubleToLongBits(f)
     *          and handle the return value like every long value;
     *      Object: use (f == null ? 0 : f.hashCode());
     *      Array: recursion and combine the values.
     *
     *  Formula:
     *      hash = prime * hash + codeForField
     */
    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + id;
        return 31 * hash + prefix;
    }

    /**
     * Compares this id to another Resource id.
     */
    @Override
    public int compareTo(final ResourceId tid) {
        if (this.prefix < tid.prefix) { return -1; }
        if (this.prefix > tid.prefix) { return 1; }
        if (this.id < tid.id) { return -1; }
        if (this.id > tid.id) { return 1; }
        return 0;
    }

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return new StringBuilder()
            .append(prefix)
            .append(':')
            .append(id)
            .toString();
    }
}
