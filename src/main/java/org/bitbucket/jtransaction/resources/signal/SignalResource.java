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


package org.bitbucket.jtransaction.resources.signal;

import org.bitbucket.jtransaction.resources.StatelessResource;


/**
 * Write a description of class SignalResource here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SignalResource extends StatelessResource
{

    /**
     * Constructor for objects of class SignalResource
     */
    public SignalResource() {
        super(new SignalInternalResource());        
    }
    
    public SignalResource(SignalInternalResource r) {
        super(r);
    }
    
    public SignalResource(SignalResource instance) {
        super(instance);
    }
    
    /*
     * The following methods are empty, this resource is only used as
     * a socket between transactions.
     */
    public void commit() {}
    public void rollback() {}
    public void update() {}
    
    public SignalResource clone() { return new SignalResource(this); }
}
