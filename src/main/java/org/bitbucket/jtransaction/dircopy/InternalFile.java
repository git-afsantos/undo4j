package org.bitbucket.jtransaction.dircopy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.bitbucket.jtransaction.common.Copyable;
import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.NormalState;
import org.bitbucket.jtransaction.resources.ResourceState;

/**
 * InternalFile
 * 
 * @author afs
 * @version 2013
*/

public final class InternalFile
        implements InternalResource<String>, Copyable<InternalFile> {
    // instance variables
    private final File file;
    private BufferedReader in = null;
    private PrintWriter out = null;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class InternalFile. */
    public InternalFile(String path) {
        file = new File(path);
    }

    /** Parameter constructor of objects of class InternalFile. */
    public InternalFile(String path, boolean allowExistence) {
        file = new File(path);
        if (!allowExistence && file.exists()) {
            throw new IllegalArgumentException(path);
        }
    }


    /** Copy constructor of objects of class InternalFile. */
    private InternalFile(InternalFile instance) {
        file = instance.getFile();
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    private File getFile() { return file; }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    @Override
    public boolean isValidState(ResourceState<String> state) {
        return true;
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    public void initialize() {}

    /** */
    public void dispose() throws IOException {
        closeInput();
        closeOutput();
    }

    /** */
    @Override
    public ResourceState<String> buildState() throws IOException {
        openInput();
        String line = in.readLine();
        return line != null ? new NormalState<String>(line) : null;
    }

    /** */
    @Override
    public void applyState(ResourceState<String> state) throws FileNotFoundException {
        if (isValidState(state)) {
            openOutput();
            out.println(state.get());
        }
    }


    /** */
    public void delete() {
        try {
            closeInput();
            closeOutput();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        file.delete();
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    private void openInput() throws FileNotFoundException {
        if (in == null) {
            in = new BufferedReader(new FileReader(file));
        }
    }

    /** */
    private void openOutput() throws FileNotFoundException {
        if (out == null) {
            out = new PrintWriter(file);
        }
    }

    /** */
    private void closeInput() throws IOException {
        if (in != null) {
            in.close();
            in = null;
        }
    }

    /** */
    private void closeOutput() throws IOException {
        if (out != null) {
            out.flush();
            out.close();
            out = null;
        }
    }



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Equivalence relation. Contract (for any non-null reference values
            x, y, and z):
        * Reflexive: x.equals(x).
        * Symmetric: x.equals(y) iff y.equals(x).
        * Transitive: if x.equals(y) and y.equals(z), then x.equals(z).
        * Consistency: successive calls (with no modification of the equality
            fields) return the same result.
        * x.equals(null) should return false.
    */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        InternalFile n = (InternalFile) o;
        return (file == n.getFile());
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
        return file.hashCode();
    }

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        return file.getName();
    }

    /** Creates and returns a (deep) copy of this object. */
    @Override
    public InternalFile clone() { return new InternalFile(this); }
}
