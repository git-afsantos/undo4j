package com.github.undo4j.bank;


/**
 * Account
 * 
 * @author afs
 * @version 2013
 */

public class Account {

    /*************************************************************************\
     *  Fields
    \*************************************************************************/

    private static int idGenerator = 1;



    /*************************************************************************\
     *  Attributes
    \*************************************************************************/

    private final int id;
    private int money;



    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /** Parameter constructor of objects of class Account. */
    private Account(int id, int money) {
        this.id     = id;
        this.money  = money;
    }


    /** Factory method */
    public static Account newInstance(int money) {
        return new Account(idGenerator++, money);
    }



    /*************************************************************************\
     *  Getters
    \*************************************************************************/

    /** */
    public int getMoney() { return money; }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public void withdraw(int amount) {
        if (money < amount) { throw new RuntimeException("No money!"); }
        money -= amount;
    }

    /** */
    public void deposit(int amount) { money += amount; }



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
        if (!(o instanceof Account)) { return false; }
        Account n = (Account) o;
        return id == n.id;
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
    public int hashCode() { return id; }

    /**
     *  Returns a string representation of the object.
     */
    @Override
    public String toString() {
        return new StringBuilder("Account(#")
            .append(id)
            .append(',')
            .append(money)
            .append(')')
            .toString();
    }
}
