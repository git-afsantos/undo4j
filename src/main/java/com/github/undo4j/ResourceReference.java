package com.github.undo4j;

/**
 * Identifier for a registered resource.
 * 
 * @author afs
 * @version 2014-01-19
 */

public final class ResourceReference<V> {

    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    /** Unique identifier. */
    private final ResourceId id;

    /** Reference to the resource. */
    private final V resource;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /**
     *  Parameter constructor of objects of class ResourceReference.
     */
    ResourceReference(final ResourceId id, final V resource) {
        this.id = id;
        this.resource = resource;
    }



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    public V get() { return resource; }

    /** */
    public ResourceId id() { return id; }



    /*************************************************************************\
     *  Equals, HashCode, ToString & Clone
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
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ResourceReference)) { return false; }
        ResourceReference<?> n = (ResourceReference<?>) o;
        return id.equals(n.id) && resource == n.resource;
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
        return 37 * id.hashCode() + resource.hashCode();
    }

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return new StringBuilder("ResourceReference#")
            .append(id)
            .toString();
    }
}
