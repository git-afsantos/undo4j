package org.jtransaction.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.bitbucket.jtransaction.common.LockManagers;
import org.bitbucket.jtransaction.resources.implementations.Action;
import org.bitbucket.jtransaction.resources.implementations.MongoCollectionInterface;
import org.bitbucket.jtransaction.resources.implementations.MongoResource;
import org.bitbucket.jtransaction.transactions.ManagedResource;
import org.bitbucket.jtransaction.transactions.TransactionManager;
import org.bitbucket.jtransaction.transactions.TransactionManagers;
import org.jtransaction.mongodb.dataaccess.SnapshotDAO;
import org.jtransaction.mongodb.dataaccess.SystemDAO;
import org.jtransaction.mongodb.datamodel.SnapshotObject;
import org.jtransaction.mongodb.datamodel.SystemObject;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
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
		MongoTransactionDemo mongoTransaction = new MongoTransactionDemo();

		buildDataObjectsForAdding(false);
		// buildDataObjectsForAdding(true);

		mongoTransaction.addSnapshots(systems, snapshots);

		// buildDataObjectsForDelete(false);
		// // buildDataObjectsForDelete(true);
		// mongoTransaction.deleteObjects(systems, snapshots);
	}

	public void addSnapshots(List<SystemObject> systems,
			List<SnapshotObject> snapshots) throws InterruptedException,
			ExecutionException, UnknownHostException {
		MongoCollectionInterface<SystemObject, SystemDAO> systemCollection = new MongoCollectionInterface<>(
				systemDAO, systems, Action.WRITE);
		MongoCollectionInterface<SnapshotObject, SnapshotDAO> snapshotCollection = new MongoCollectionInterface<>(
				snapshotDAO, snapshots, Action.WRITE);

		TransactionManager tm = TransactionManagers.newSynchronousManager();

		AddSnapshotsTransaction transaction = new AddSnapshotsTransaction(
				systemCollection, snapshotCollection,
				ManagedResource
						.from(new MongoResource<SystemObject, SystemDAO>(
								new SystemResource())),
				ManagedResource
						.from(new MongoResource<SnapshotObject, SnapshotDAO>(
								new SnapshotResource())));
		Future<Boolean> f = tm.submit(transaction);

		java.lang.System.out.println(f.get().booleanValue());
	}

	public void deleteObjects(List<SystemObject> systems,
			List<SnapshotObject> snapshots) throws InterruptedException,
			ExecutionException {
		MongoCollectionInterface<SystemObject, SystemDAO> systemCollection = new MongoCollectionInterface<>(
				systemDAO, systems, Action.DELETE);
		MongoCollectionInterface<SnapshotObject, SnapshotDAO> snapshotCollection = new MongoCollectionInterface<>(
				snapshotDAO, snapshots, Action.DELETE);

		TransactionManager tm = TransactionManagers.newSynchronousManager();

		DeletObjectsTransaction transaction = new DeletObjectsTransaction(
				systemCollection, snapshotCollection,
				ManagedResource
						.from(new MongoResource<SystemObject, SystemDAO>(
								new SystemResource())),
				ManagedResource
						.from(new MongoResource<SnapshotObject, SnapshotDAO>(
								new SnapshotResource())));
		Future<Boolean> f = tm.submit(transaction);

		java.lang.System.out.println(f.get().booleanValue());
	}

	public SystemObject readSystem(String systemID) throws Exception {
		List<SystemObject> system = new ArrayList<>(1);
		system.add(new SystemObject(systemID));

		SystemResource systemResource = new SystemResource();

		MongoCollectionInterface<SystemObject, SystemDAO> systemCollection = new MongoCollectionInterface<SystemObject, SystemDAO>(
				systemDAO, system, Action.READ);

		TransactionManager tm = TransactionManagers.newSynchronousManager();
		ReadSystemTransaction transaction = new ReadSystemTransaction(
				systemCollection,
				ManagedResource
						.from(new MongoResource<SystemObject, SystemDAO>(
								systemResource, LockManagers
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
