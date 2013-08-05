-------------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all he/she needs to know. The comments should usually include at least:
-------------------------------------------------------------------------------

PROJECT TITLE: AtomicUM - Library for Atomicity and Transactional Behaviour
PURPOSE OF PROJECT: Extending Transaction Handling to Application Logic
VERSION or DATE: v0
HOW TO START THIS PROJECT:
AUTHORS: André Santos, Victor Miraldo
USER INSTRUCTIONS:


-----
Some ideas:
    * Transaction object wraps the user Callable.
    * Transactions will also be callables, to pass to the executor.
    * Transactions do some setup and cleanup code, wrapping the user code.
    * Transactions must have a reference to some interface of the resource manager.
        * This will let the transaction inspect behaviour after execution.
    * Setup - Transaction binds current thread to resource type.
        * This prevents the user from changing resource type during execution.
    * Setup - Transaction binds current thread to read or write.
        * This tells the resource manager which handler to provide (read/write).
    * There's no way to determine at compile time if the user code uses resources.
        * This is due to allowing free code from the user.
    * Free code hints at letting the user manipulate the resource directly.
        * This raises problems for readers that may write.
    * Alternative: provide wrappers for common resources, and only writers
        (all permissions) to user-defined objects.
    * Cleanup - Transaction should know how many operations were performed.
        * Transaction throws exception if no operation was performed.
    * Cleanup - If an exception occurred, and transaction is writer, transaction
        calls rollback for every resource used, on the manager interface.
        Otherwise, transaction calls commit for every resource used.
    * Bedtime story: resource manager running in separate thread pool.
    * Having a Thread owner of a transaction manager.
        * This allows skipping locks in a synchronous manager.
    * Must see: java.util.concurrent.locks.ReentrantReadWriteLock.
    * Must see: Docs for ThreadLocal, for example on how to create transaction ids.

Managing resources:
    * The first time a read or write is called on the resource, the lock is requested.
    * If lock is acquired, resource should tell manager that the thread requested it.
        * This allows transactions to commit or rollback later on.
    * Each resource type should have its State implementation.
    * Adaptation of Memento Pattern:
        * When a resource is read, it returns (a copy of) its state (memento).
        * To write, a resource accepts mementos, and stores them.
        * The merging with the internal state occurs on commit.
    * Resources may be buffered or direct (memento vs list of mementos).
    * When committing, no one should be able to acquire read or write locks.
    * Adaptation of Strategy Pattern:
        * Every resource has a strategy to read / write on its (internal) resource.
        * For custom resources, the user provides the strategy.
