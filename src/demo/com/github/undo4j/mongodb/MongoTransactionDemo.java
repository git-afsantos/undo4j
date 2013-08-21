package com.github.undo4j.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.github.undo4j.common.LockManagers;
import com.github.undo4j.mongodb.dataaccess.SnapshotDAO;
import com.github.undo4j.mongodb.dataaccess.SystemDAO;
import com.github.undo4j.mongodb.datamodel.SnapshotObject;
import com.github.undo4j.mongodb.datamodel.SystemObject;
import com.github.undo4j.resources.implementations.MongoAction;
import com.github.undo4j.resources.implementations.MongoCollectionInterface;
import com.github.undo4j.resources.implementations.MongoResource;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionManager;
import com.github.undo4j.transactions.TransactionManagers;
import com.mongodb.MongoClient;

public class MongoTransactionDemo {

	private Datastore datastore;

	private SystemDAO systemDAO;
	private SnapshotDAO snapshotDAO;

	private static String system1 = "system1";
	private static String system2 = "system2";
	private static String snapshot1 = "snapshot1";
	private static String snapshot2 = "snapshot2";

	private static List<SystemObject> systems = new ArrayList<SystemObject>();
	private static List<SnapshotObject> snapshots = new ArrayList<SnapshotObject>();

	public MongoTransactionDemo() throws UnknownHostException {
		datastore = initMongo();
		systemDAO = new SystemDAO(datastore);
		snapshotDAO = new SnapshotDAO(datastore);
	}

	public static void main(String[] args) throws Exception {
		MongoTransactionDemo demo = new MongoTransactionDemo();

		buildDataObjectsForAdding(false);
		// buildDataObjectsForAdding(true);

		demo.addSnapshots(systems, snapshots);

		// buildDataObjectsForDelete(false);
		// // buildDataObjectsForDelete(true);
		// mongoTransaction.deleteObjects(systems, snapshots);
	}

	public void addSnapshots(List<SystemObject> systems, List<SnapshotObject> snapshots) throws InterruptedException,
			ExecutionException, UnknownHostException {
		MongoCollectionInterface<SystemObject, SystemDAO> systemCollection = new MongoCollectionInterface<>(systemDAO,
				systems, MongoAction.WRITE);
		MongoCollectionInterface<SnapshotObject, SnapshotDAO> snapshotCollection = new MongoCollectionInterface<>(
				snapshotDAO, snapshots, MongoAction.WRITE);

		AddSnapshotsTransaction transaction = new AddSnapshotsTransaction(systemCollection, snapshotCollection,
				ManagedResource.from(new MongoResource<SystemObject, SystemDAO>(new SystemResource())),
				ManagedResource.from(new MongoResource<SnapshotObject, SnapshotDAO>(new SnapshotResource())));

		TransactionManager tm = TransactionManagers.newSynchronousManager();
		Future<Boolean> f = tm.submit(transaction);

		java.lang.System.out.println(f.get().booleanValue());
	}

	public void deleteObjects(List<SystemObject> systems, List<SnapshotObject> snapshots) throws InterruptedException,
			ExecutionException {
		MongoCollectionInterface<SystemObject, SystemDAO> systemCollection = new MongoCollectionInterface<>(systemDAO,
				systems, MongoAction.DELETE);
		MongoCollectionInterface<SnapshotObject, SnapshotDAO> snapshotCollection = new MongoCollectionInterface<>(
				snapshotDAO, snapshots, MongoAction.DELETE);

		TransactionManager tm = TransactionManagers.newSynchronousManager();

		DeleteObjectsTransaction transaction = new DeleteObjectsTransaction(systemCollection, snapshotCollection,
				ManagedResource.from(new MongoResource<SystemObject, SystemDAO>(new SystemResource())),
				ManagedResource.from(new MongoResource<SnapshotObject, SnapshotDAO>(new SnapshotResource())));
		Future<Boolean> f = tm.submit(transaction);

		java.lang.System.out.println(f.get().booleanValue());
	}

	public SystemObject readSystem(String systemID) throws Exception {
		List<SystemObject> system = new ArrayList<>(1);
		system.add(new SystemObject(systemID));

		SystemResource systemResource = new SystemResource();

		MongoCollectionInterface<SystemObject, SystemDAO> systemCollection = new MongoCollectionInterface<SystemObject, SystemDAO>(
				systemDAO, system, MongoAction.READ);

		TransactionManager tm = TransactionManagers.newSynchronousManager();
		ReadSystemTransaction transaction = new ReadSystemTransaction(systemCollection,
				ManagedResource.from(new MongoResource<SystemObject, SystemDAO>(systemResource, LockManagers
						.newNullLockManager())));
		tm.submit(transaction);
		return systemResource.buildState().get().getObjects().get(0);
	}

	private static void buildDataObjectsForAdding(boolean error) {
		clearLists();

		systems.add(new SystemObject(system1));
		systems.add(new SystemObject(system2));

		snapshots.add(new SnapshotObject(system1, snapshot1));
		snapshots.add(new SnapshotObject(system1, snapshot2));
		snapshots.add(new SnapshotObject(system2, snapshot1));
		if (error) {
			snapshots.add(new SnapshotObject("system3", snapshot1));
		}
	}

	@SuppressWarnings("unused")
	private static void buildDataObjectsForDelete(boolean error) {
		clearLists();

		systems.add(new SystemObject(system1));
		if (error) {
			systems.add(new SystemObject(system2));
		}

		snapshots.add(new SnapshotObject(system1, snapshot1));
		snapshots.add(new SnapshotObject(system1, snapshot2));

	}

	private static void clearLists() {
		systems.clear();
		snapshots.clear();
	}

	private Datastore initMongo() throws UnknownHostException {
		MongoClient mongo = new MongoClient("localhost", 27017);
		Morphia morphia = new Morphia();
		Datastore datastore = morphia.createDatastore(mongo, "test");
		return datastore;
	}
}
