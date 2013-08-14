package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.AccessMode;
import org.bitbucket.jtransaction.common.IsolationLevel;

import org.bitbucket.jtransaction.resources.Resource;

import static org.bitbucket.jtransaction.common.Check.checkArgument;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * AbstractTransactionManager
 * 
 * @author afs
 * @version 2013
*/

abstract class AbstractTransactionManager
        implements TransactionManager, TransactionListener {
    // instance variables
    private final ResourceManager resourceManager;
    private final ThreadLocal<Transaction<?>> transactions =
            new ThreadLocal<Transaction<?>>();

    /**************************************************************************
     * Constructors
    **************************************************************************/

    /** Parameter constructor of objects of class AbstractTransactionManager.
     */
    protected AbstractTransactionManager(ResourceManager rm) {
        checkArgument("null resource manager", rm);
        this.resourceManager = rm;
    }



    /**************************************************************************
     * Getters
    **************************************************************************/

    /** */
    @Override
    public final ResourceHandle getHandleFor(String resource) {
        // Only transactions may use this method.
        checkTransaction();
        // Retrieve resource.
        Resource r = getResource(resource);
        // Retrieve the current transaction.
        Transaction<?> t = getTransaction();
        // Create new controller for resource.
        ResourceController ctrl;
        if (t.getAccessMode() == AccessMode.READ) {
            ctrl = new ReadOnlyController(r, resource, t);
        } else {
            ctrl = new ResourceController(r, resource, t);
        }
        // Register new controller.
        t.putController(resource, ctrl);
        return ctrl;
    }


    /** */
    protected final ResourceManager getResourceManager() {
        return this.resourceManager;
    }

    /** Override for concurrent access. */
    protected Resource getResource(String id) {
        return this.resourceManager.getResource(id);
    }

    /** */
    protected final Transaction<?> getTransaction() {
        return this.transactions.get();
    }



    /**************************************************************************
     * Setters
    **************************************************************************/

    /** Sets the current thread as the given transaction. */
    protected final <T> void setTransaction(Transaction<T> t) {
        this.transactions.set(t);
    }



    /**************************************************************************
     * Predicates
    **************************************************************************/

    /** Checks whether the current thread is a transaction.
     */
    protected final boolean isTransaction() {
        return this.transactions.get() != null;
    }



    /**************************************************************************
     * Public Methods
    **************************************************************************/

    /** Only non-transactional threads may call this method. */
    @Override
    public void putResource(String id, Resource resource) {
        // Throw exception if a transaction tries to add a resource.
        checkNotTransaction();
        // Delegate to resource manager.
        this.resourceManager.putResource(id, resource);
    }

    /** Only non-transactional threads may call this method. */
    @Override
    public void removeResource(String id) {
        // Throw exception if a transaction tries to remove a resource.
        checkNotTransaction();
        // Delegate to resource manager.
        this.resourceManager.removeResource(id);
    }


    /** Submits the transaction of execution.
     * Assumes default access mode: write.
     * Assumes default isolation level: none.
     */
    @Override
    public <T> Future<TransactionResult<T>> submit(Callable<T> task) {
        return submit(task, AccessMode.WRITE, IsolationLevel.NONE);
    }

    /** Submits the transaction for execution.
     * Assumes default isolation level: none.
     */
    @Override
    public <T> Future<TransactionResult<T>> submit(
        Callable<T> task, AccessMode mode
    ) {
        return submit(task, mode, IsolationLevel.NONE);
    }


    /** Registers the current thread as a transaction. */
    @Override
    public <T> void bind(Transaction<T> t) {
        this.transactions.set(t);
    }

    /** Unregisters the current thread as a transaction. */
    @Override
    public void terminate() {
        this.transactions.remove();
    }



    /**************************************************************************
     * Private Methods
    **************************************************************************/

    /** */
    protected final void checkTransaction() {
        if (!isTransaction()) {
            throw new TransactionContextException("not a transaction");
        }
    }


    /** */
    protected final void checkNotTransaction() {
        if (isTransaction()) {
            throw new TransactionContextException("thread is a transaction");
        }
    }



    /**************************************************************************
     * Equals, HashCode, ToString & Clone
    **************************************************************************/

    /** Returns a string representation of the object. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.resourceManager);
        return sb.toString();
    }
}
