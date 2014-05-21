package com.github.undo4j.file;

import com.github.undo4j.*;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

/**
 * Installer
 * 
 * @author afs
 * @version 2013
 */

public class Installer {

    /*************************************************************************\
     *  Constructors
    \*************************************************************************/

    /** Empty constructor of objects of class Installer. */
    private Installer() { throw new AssertionError(); }



    /*************************************************************************\
     *  Public Methods
    \*************************************************************************/

    /** */
    public static void test() {
        Gui gui = new Gui();
        if (!startGui(gui)) { return; }

        TransactionManager man = TransactionManagers.newSingleThreadManager();
        ResourceReference<File> root = man.register(root());
        List<ResourceReference<File>> files = new ArrayList<>();
        for (File f: files(root.get().toString())) {
            files.add(man.register(f));
        }

        try {
            man.execute(new GoodInstaller(root, files, gui));
            gui.update(1, "Installation successful.");
        } catch (TransactionExecutionException ex) {
            gui.update(0, "Installation failed. Files deleted.");
            ex.printStackTrace();
        } finally {
            man.shutdown();
        }
    }


    /** */
    public static void testRollback() {
        Gui gui = new Gui();
        if (!startGui(gui)) { return; }

        TransactionManager man = TransactionManagers.newSingleThreadManager();
        ResourceReference<File> root = man.register(root());
        List<ResourceReference<File>> files = new ArrayList<>();
        for (File f: files(root.get().toString())) {
            files.add(man.register(f));
        }

        try {
            man.execute(new BadInstaller(root, files, gui));
            gui.update(1, "Installation successful.");
        } catch (TransactionExecutionException ex) {
            gui.update(0, "Installation failed. Files deleted.");
            ex.printStackTrace();
        } finally {
            man.shutdown();
        }
    }



    /*************************************************************************\
     *  Private Methods
    \*************************************************************************/

    /** */
    private static boolean startGui(Gui gui) {
        try {
            gui.start();
            return gui.waitForSignal();
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    /** */
    private static File root() {
        return new File(System.getProperty("user.home")
            + File.separator + ".InstallerDemo");
    }


    /** */
    private static File[] files(String root) {
        root = root + File.separator;
        return new File[]{
            new File(root + "readme.txt"),
            new File(root + "license.txt"),
            new File(root + "copyright.txt"),
            new File(root + "tutorial.txt")
        };
    }



    /*************************************************************************\
     *  Nested Classes
    \*************************************************************************/

    /** */
    private static class GoodInstaller implements ClientRunnable {
        private final ResourceReference<File> dir;
        private final List<ResourceReference<File>> files;
        private final UpdateListener updater;

        GoodInstaller(ResourceReference<File> dir,
                List<ResourceReference<File>> files,
                UpdateListener updater) {
            this.dir = dir;
            this.files = files;
            this.updater = updater;
        }

        @Override
        public void run(OperationDispatcher dispatcher) {
            int size = files.size(), i = 0;
            dispatcher.write(FileOperations.makeDir(dir));
            for (ResourceReference<File> f: files) {
                sleep();
                dispatcher.write(FileOperations.write(f, "HELLO WORLD"));
                updater.update((double) ++i / size,
                    "Installed - " + f.get().toString());
            }
        }

        private void sleep() {
            try { Thread.sleep(2000L); }
            catch (InterruptedException ex) {
                throw new RuntimeException("Installation interrupted.");
            }
        }
    }


    /** */
    private static class BadInstaller implements ClientRunnable {
        private final ResourceReference<File> dir;
        private final List<ResourceReference<File>> files;
        private final UpdateListener updater;

        BadInstaller(ResourceReference<File> dir,
                List<ResourceReference<File>> files,
                UpdateListener updater) {
            this.dir = dir;
            this.files = files;
            this.updater = updater;
        }

        @Override
        public void run(OperationDispatcher dispatcher) {
            dispatcher.write(FileOperations.makeDir(dir));
            for (int i = 0, size = files.size(); i < size - 1; ++i) {
                sleep();
                ResourceReference<File> f = files.get(i);
                dispatcher.write(FileOperations.write(f, "HELLO WORLD"));
                updater.update((double) (i + 1) / size,
                    "Installed - " + f.get().toString());
            }
            throw new RuntimeException("Rollback requested.");
        }

        private void sleep() {
            try { Thread.sleep(2000L); }
            catch (InterruptedException ex) {
                throw new RuntimeException("Installation interrupted.");
            }
        }
    }
}
