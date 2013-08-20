package com.github.undo4j.dircopy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionManager;
import com.github.undo4j.transactions.TransactionManagers;

/**
 * InstallerDemo
 * 
 * @author afs
 * @version 2013
*/

public final class InstallerDemo {
    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class InstallerDemo. */
    private InstallerDemo() {}



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    public static void run(boolean fails) {
        File root = getRoot();
        
        String[] files = getFiles(root.toString());
        Gui gui = new Gui();
        
        if (startGui(gui)) {
            makeDirectory(root);
            
            TransactionManager manager;
            manager = TransactionManagers.newSynchronousManager();
            List<ManagedResource<String>> rs = new ArrayList<>(files.length);
            for (String f : files) {
            	rs.add(ManagedResource.from(new FileResource(f, false)));
            }
            
            try {
                String result = manager.submit(
                    new Installer(fails, files, gui, rs)
                ).get();
                gui.update(1, result);
            } catch (ExecutionException ex) {
                gui.update(0, ex.getCause().getMessage());
                root.delete();
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
            
            manager.shutdown();
        }
    }
    
    
    public static void main(String[] args) {
    	boolean fails = args.length > 0 ? Boolean.parseBoolean(args[0]) : false;
    	run(fails);
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

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
    private static File getRoot() {
        return new File(
            System.getProperty("user.home") + File.separator + ".InstallerDemo"
        );
    }

    /** */
    private static void makeDirectory(File root) {
        if (!root.exists()) { root.mkdir(); }
        else if (!root.isDirectory()) {
            throw new RuntimeException("Path collision");
        }
    }

    /** */
    private static String[] getFiles(String root) {
        root = root + File.separator;
        return new String[] {
            root + "readme.txt", root + "license.txt",
            root + "copyright.txt", root + "tutorial.txt"
        };
    }
}
