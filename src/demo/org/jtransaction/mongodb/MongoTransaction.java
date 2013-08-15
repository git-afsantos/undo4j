package org.jtransaction.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.bitbucket.jtransaction.common.LockManagers;
import org.bitbucket.jtransaction.resources.ImmutableState;
import org.bitbucket.jtransaction.resources.implementations.MongoResource;
import org.bitbucket.jtransaction.transactions.ManagedResource;
import org.bitbucket.jtransaction.transactions.TransactionManager;
import org.bitbucket.jtransaction.transactions.TransactionManagers;
import org.bitbucket.jtransaction.transactions.TransactionalCallable;
import org.jtransaction.mongodb.dataaccess.SnapshotDAO;
import org.jtransaction.mongodb.dataaccess.SystemDAO;
import org.jtransaction.mongodb.datamodel.SnapshotObject;
import org.jtransaction.mongodb.datamodel.SystemObject;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Morphia;
import com.mongodb.MongoClient;

public class MongoTransaction {

	private Datastore datastore;

	public static void main(String[] args) throws Exception {
		MongoTransaction mongoTransaction = new MongoTransaction();

		String system1 = "system1";
		String system2 = "system2";
		List<SystemObject> systems = new ArrayList<SystemObject>();
		systems.add(new SystemObject(system1));
		systems.add(new SystemObject(system2));
		List<SnapshotObject> snapshots = new ArrayList<SnapshotObject>();
		snapshots.add(new SnapshotObject(system1, "snapshot1"));
		snapshots.add(new SnapshotObject(system1, "snapshot2"));
		snapshots.add(new SnapshotObject(system2, "snapshot1"));
		snapshots.add(new SnapshotObject("system3", "snapshot1"));
		mongoTransaction.addSnapshots(systems, snapshots);
	}

	public MongoTransaction() throws UnknownHostException {
		datastore = initMongo();
	}

	public SystemObject readSystem(String systemID) throws Exception {
		List<SystemObject> system = new ArrayList<>(1);
		system.add(new SystemObject(systemID));

		SystemDAO systemDAO = new SystemDAO(datastore);
		SystemResource systemResource = new SystemResource(systemDAO);

		MongoCollection<SystemObject, SystemDAO> systemCollection = new MongoCollection<SystemObject, SystemDAO>(
				systemDAO, system, Action.READ);

		ManagedResource<MongoCollection<SystemObject, SystemDAO>> systemManagedResource = ManagedResource
				.from(new MongoResource<SystemObject, SystemDAO>(
						systemResource, LockManagers.newNullLockManager()));

		TransactionManager tm = TransactionManagers.newSynchronousManager();
		tm.submit(new ReadSystemTransaction(systemCollection,
				systemManagedResource));
		return systemResource.buildState().get().getObjects().get(0);
	}

	public void addSnapshots(List<SystemObject> systems,
			List<SnapshotObject> snapshots) throws InterruptedException,
			ExecutionException, UnknownHostException {
		SystemDAO systemDAO = new SystemDAO(datastore);
		SnapshotDAO snapshotDAO = new SnapshotDAO(datastore);

		MongoCollection<SystemObject, SystemDAO> systemCollection = new MongoCollection<>(
				systemDAO, systems, Action.WRITE);
		MongoCollection<SnapshotObject, SnapshotDAO> snapshotCollection = new MongoCollection<>(
				snapshotDAO, snapshots, Action.WRITE);

		SystemResource systemResource = new SystemResource(systemDAO);
		SnapshotResource snapshotResource = new SnapshotResource(snapshotDAO);

		ManagedResource<MongoCollection<SystemObject, SystemDAO>> systemManagedResource = ManagedResource
				.from(new MongoResource<SystemObject, SystemDAO>(
						systemResource, LockManagers.newNullLockManager()));
		ManagedResource<MongoCollection<SnapshotObject, SnapshotDAO>> snapshotManagedResource = ManagedResource
				.from(new MongoResource<SnapshotObject, SnapshotDAO>(
						snapshotResource, LockManagers.newNullLockManager()));

		TransactionManager tm = TransactionManagers.newSynchronousManager();

		Future<Boolean> f = tm.submit(new AddSnapshotsTransaction(
				systemCollection, snapshotCollection, systemManagedResource,
				snapshotManagedResource));
		boolean b = f.get().booleanValue();

		java.lang.System.out.println(b);
	}

	private Datastore initMongo() throws UnknownHostException {
		MongoClient mongo = new MongoClient("localhost", 27017);
		Morphia morphia = new Morphia();
		Datastore datastore = morphia.createDatastore(mongo, "test");
		return datastore;
	}

	private class AddSnapshotsTransaction implements
			TransactionalCallable<Boolean> {
		private ManagedResource<MongoCollection<SystemObject, SystemDAO>> systemResource;
		private ManagedResource<MongoCollection<SnapshotObject, SnapshotDAO>> snapshotResource;
		private MongoCollection<SystemObject, SystemDAO> systems;
		private MongoCollection<SnapshotObject, SnapshotDAO> snapshots;

		public AddSnapshotsTransaction(
				MongoCollection<SystemObject, SystemDAO> systems,
				MongoCollection<SnapshotObject, SnapshotDAO> snapshots,
				ManagedResource<MongoCollection<SystemObject, SystemDAO>> systemResource,
				ManagedResource<MongoCollection<SnapshotObject, SnapshotDAO>> snapshotResource) {
			this.systemResource = systemResource;
			this.snapshotResource = snapshotResource;
			this.systems = systems;
			this.snapshots = snapshots;
		}

		@Override
		public Boolean call() throws Exception {
			systemResource
					.write(new ImmutableState<MongoCollection<SystemObject, SystemDAO>>(
							systems));
			snapshotResource
					.write(new ImmutableState<MongoCollection<SnapshotObject, SnapshotDAO>>(
							snapshots));
			return new Boolean(true);
		}

		@Override
		public Iterable<ManagedResource<?>> getManagedResources() {
			List<ManagedResource<?>> list = new ArrayList<>();
			list.add(systemResource);
			list.add(snapshotResource);
			return list;
		}

	}

	private class ReadSystemTransaction implements
			TransactionalCallable<Boolean> {
		private ManagedResource<MongoCollection<SystemObject, SystemDAO>> systemResource;
		private MongoCollection<SystemObject, SystemDAO> system;

		public ReadSystemTransaction(
				MongoCollection<SystemObject, SystemDAO> system,
				ManagedResource<MongoCollection<SystemObject, SystemDAO>> systemResource) {
			this.systemResource = systemResource;
			this.system = system;
		}

		@Override
		public Boolean call() throws Exception {
			systemResource.write(new ImmutableState<>(system));
			return new Boolean(true);
		}

		@Override
		public Iterable<ManagedResource<?>> getManagedResources() {
			List<ManagedResource<?>> list = new ArrayList<>();
			list.add(systemResource);
			return list;
		}

	}

}
