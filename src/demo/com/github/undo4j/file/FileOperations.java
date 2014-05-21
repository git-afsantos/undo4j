package com.github.undo4j.file;

import com.github.undo4j.*;

import java.io.*;

import java.util.Arrays;

/**
 * FileOperations
 * 
 * @author afs
 * @version 2013
 */

public class FileOperations {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /** Empty constructor of objects of class FileOperations. */
    private FileOperations() { throw new AssertionError(); }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static WriteOperation write
            (ResourceReference<File> file, String text) {
        return new TextWriter(file, text);
    }


    /** */
    public static WriteOperation makeDir(ResourceReference<File> dir) {
        return new DirectoryMaker(dir);
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /** */
    private static class TextWriter implements WriteOperation {
        private final File file;
        private final ResourceId id;
        private final String text;

        TextWriter(ResourceReference<File> file, String text) {
            this.file = file.get();
            this.id = file.id();
            this.text = text;
        }

        @Override
        public void write() {
            try (PrintWriter out = new PrintWriter(file)) {
                out.println(text);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException
                    ("Can't write to file: " + file.toString());
            }
        }

        @Override
        public void undo() {
            if (file.exists()) {
                file.delete();
            }
        }

        @Override
        public Iterable<ResourceId> resources() {
            return Arrays.asList(id);
        }
    }


    /** */
    private static class DirectoryMaker implements WriteOperation {
        private final File dir;
        private final ResourceId id;
        private boolean created = false;

        DirectoryMaker(ResourceReference<File> dir) {
            this.dir = dir.get();
            this.id = dir.id();
        }

        @Override
        public void write() {
            if (dir.exists()) {
                throw new RuntimeException("Already exists: " + dir.toString());
            }
            dir.mkdir();
            created = true;
        }

        @Override
        public void undo() {
            if (created) {
                dir.delete();
            }
        }

        @Override
        public Iterable<ResourceId> resources() {
            return Arrays.asList(id);
        }
    }
}
