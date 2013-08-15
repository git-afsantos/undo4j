package org.bitbucket.jtransaction.dircopy;

/**
 * Installer
 * 
 * @author afs
 * @version 2013
*/

public final class Installer implements Callable<String> {
    private static final String TEXT = "Hello World!";

    // instance variables
    private final boolean canFail;
    private final String[] files;
    private final UpdateListener listener;
    private final ResourceHandleProvider provider;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class Installer. */
    public Installer(
        boolean fails,
        String[] files, UpdateListener listener,
        ResourceHandleProvider provider
    ) {
        this.canFail = fails;
        this.files = files;
        this.listener = listener;
        this.provider = provider;
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    // ...



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /**
     * This is our transaction body!
     */
    @Override
    public String call() throws InterruptedException {
        StringState text = new StringState(TEXT);
        ResourceHandle file;
        for (int i = 0; i < files.length; ++i) {
            // Retrieve file from manager.
            file = provider.getHandleFor(files[i]);
            Thread.sleep(2000L);
            if (canFail && i == files.length - 1) { fail(files[i]); }
            
            // Write the contents of the file.
            file.write(text);
            // Update the GUI's progress bar.
            listener.update(
                (double) (i + 1) / files.length,
                "Installed - " + files[i]
            );
        }
        return "Installation complete!";
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    private void fail(String file) {
        throw new RuntimeException("Failed - " + file);
    }
}
