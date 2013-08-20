package com.github.undo4j.dircopy;

import java.util.ArrayList;
import java.util.List;

import com.github.undo4j.resources.NormalState;
import com.github.undo4j.resources.ResourceState;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionalCallable;

/**
 * Installer
 * 
 * @author afs
 * @version 2013
*/

public final class Installer implements TransactionalCallable<String> {
    private static final String TEXT = "Hello World!";

    // instance variables
    private final boolean canFail;
    private final String[] files;
    private final UpdateListener listener;
    private final List<ManagedResource<String>> resources;

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class Installer. */
    public Installer(
        boolean fails,
        String[] files, UpdateListener listener,
        List<ManagedResource<String>> resources
    ) {
        this.canFail = fails;
        this.files = files;
        this.listener = listener;
        this.resources = resources;
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
        ResourceState<String> text = new NormalState<String>(TEXT);
        ManagedResource<String> file;
        for (int i = 0; i < files.length; ++i) {
            // Retrieve file from manager.
            file = resources.get(i);
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
    
    
    /** */
    @Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> list = new ArrayList<>(resources.size());
		for (ManagedResource<?> r: resources) {
			list.add(r);
		}
    	return list;
	}



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    private void fail(String file) {
        throw new RuntimeException("Failed - " + file);
    }
}
