/*
The MIT License (MIT)

Copyright (c) 2013 Andre Santos, Victor Miraldo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/


package org.bitbucket.afsantos.jtransaction.transactions;

import org.bitbucket.afsantos.jtransaction.resources.Resource;

import static org.bitbucket.afsantos.jtransaction.common.Check.checkArgument;

import java.util.HashMap;
import java.util.Map;

/**
 * ResourceManager
 * 
 * @author afs
 * @version 2013
*/

class ResourceManager implements ResourcePool {
    private static final String REGISTERED =    "key already registered";
    private static final String UNREGISTERED =  "unknown resource";

    // instance variables
    private boolean logging = false;
    private final Map<String, Resource> resources = new HashMap<>();
    private final Map<String, ResourceStatistics> stats = new HashMap<>();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Empty constructor of objects of class ResourceManager. */
    ResourceManager() {}


    /** Parameter constructor of objects of class ResourceManager. */
    ResourceManager(boolean logStatistics) { logging = logStatistics; }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    Resource getResource(String id) {
        // Make sure the resource exists, before returning it.
        checkResourceExists(id);
        return resources.get(id);
    }


    /** */
    ResourceStatistics getStatistics(String id) {
        // Make sure the resource exists, before returning the statistics.
        checkResourceExists(id);
        return logging ? stats.get(id) : new ResourceStatistics();
    }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** */
    void setLoggingStatistics(boolean log) {
        logging = log;
        if (!log) { stats.clear(); }
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** */
    boolean isLoggingStatistics() { return logging; }


    /** */
    boolean hasResource(String id) { return resources.containsKey(id); }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** */
    @Override
    public void putResource(String id, Resource resource) {
        // Throw exception if the resource is null.
        checkArgument(resource);
        // Throw exception if the key is already registered.
        checkResourceNotExists(id);
        // Initialize resource, before adding to pool.
        resource.initialize();
        resources.put(id, resource);
        // If logging statistics, add empty statistics to new resource.
        if (logging) { stats.put(id, new ResourceStatistics()); }
    }

    /** */
    @Override
    public void removeResource(String id) {
        // Retrieve and dispose resource, before removing from pool.
        getResource(id).dispose();
        resources.remove(id);
        // If logging statistics, remove statistics for removed resource.
        if (logging) { stats.remove(id); }
    }


    /** */
    void addStatistics(String id, ResourceStatistics rs) {
        if (logging) {
            // Make sure the resource exists.
            checkResourceExists(id);
            // Add to existing statistics.
            stats.get(id).add(rs);
        }
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    private void checkResourceExists(String id) {
        if (!resources.containsKey(id)) {
            throw new KeyUnregisteredException(UNREGISTERED);
        }
    }

    /** */
    private void checkResourceNotExists(String id) {
        if (resources.containsKey(id)) {
            throw new KeyRegisteredException(REGISTERED);
        }
    }



    /**************************************************************************
     * ToString
    **************************************************************************/

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(resources.toString());
        return sb.toString();
    }
}
