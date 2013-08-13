package org.bitbucket.jtransaction.transactions;


/**
 * ReadWriteListener
 * 
 * @author afs
 * @version 2013
*/

interface ReadWriteListener {
    void readPerformed(String resource);
    void writePerformed(String resource);
}
