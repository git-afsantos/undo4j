package org.bitbucket.jtransaction.dircopy;

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
            manager = TransactionManagers.newSingleThreadSyncManager();
            for (String f : files) {
                manager.putResource(f, new FileResource(f, false));
            }
            
            try {
                String result = manager.submit(
                    new Installer(fails, files, gui, manager)
                ).get().getResult();
                gui.update(1, result);
            } catch (ExecutionException ex) {
                gui.update(0, ex.getCause().getMessage());
                root.delete();
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
            
            for (String f: files) { manager.removeResource(f); }
            manager.shutdown();
        }
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
