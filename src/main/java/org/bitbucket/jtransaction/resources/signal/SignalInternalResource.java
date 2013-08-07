/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2013 Andre Santos, Victor Miraldo
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.bitbucket.jtransaction.resources.signal;

import java.util.concurrent.LinkedBlockingQueue;

import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.ResourceDisposeException;
import org.bitbucket.jtransaction.resources.ResourceState;
import org.bitbucket.jtransaction.resources.ResourceWriteException;

/**
 * SignalInternalResource
 * 
 * @author vmiraldo, 
 * @version 2013
 */

public final class SignalInternalResource implements InternalResource {
    private LinkedBlockingQueue<SignalState> queue;

    /**
     * Empty constructor. There's nothing to "construct"
     */
    public SignalInternalResource() {}

    /**
     * We must allocate out queue.
     */
    public void initialize() throws Exception {
        queue = new LinkedBlockingQueue<SignalState>();
    }

    /**
     * Resource disposal.
     */
    public void dispose() throws Exception {
        //Is this behavior acceptable?
        //should every signal be handled?
        if (queue.size() != 0)
            throw new ResourceDisposeException("Unhandled Signals");
    }

    /** */
    public boolean isValidState(ResourceState state) {
        return (state instanceof SignalState);
    }

    /** Builds and returns a representation of this resource's state.
     * This method is allowed to throw any exception,
     * if a state can't be built.
     */
    public ResourceState buildState() throws Exception {
        return queue.take();
    }

    /** Applies the given state to this resource.
     * This method is allowed to throw any exception,
     * if the changes can't be applied.
     */
    public void applyState(ResourceState state) throws Exception {
        if (!(state instanceof SignalState))
            throw new ResourceWriteException("Only SignalStates are allowed in SignalResources");

        queue.put((SignalState)state);
    }
}
