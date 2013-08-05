To do:
    * Transaction - interrupt any ongoing operation on interrupted exception
    * Update statistics in Resource Manager
    * Use list of listeners in resource controller
    * client callable with rollback code
    * concurrent resource manager


Resource access and locking:
    1. Transaction asks manager for handler;
    2. Manager creates controller (which is also an handler);
    3. Manager adds new controller to list of controllers given to transaction;
    4. Manager returns controller (handler) to transaction;
    5. The first time controller is used (read/write), flag accessed is set;
    6. Depending on lock scheme, controller may acquire and keep lock;
    7. When transaction commits or rolls back, manager traverses controller list;
    8. For each controller, releases any lock, gathers statistics, etc.