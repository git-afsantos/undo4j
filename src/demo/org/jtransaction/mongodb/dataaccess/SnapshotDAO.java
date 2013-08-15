package org.jtransaction.mongodb.dataaccess;

import java.util.LinkedList;
import java.util.List;

import org.bitbucket.jtransaction.resources.implementations.MongoDAO;
import org.jtransaction.mongodb.datamodel.SnapshotObject;
import org.jtransaction.mongodb.datamodel.SystemObject;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;

public class SnapshotDAO extends BasicDAO<SnapshotObject, String> implements
		MongoDAO<SnapshotObject> {

	public SnapshotDAO(Datastore datastore) {
		super(datastore);
	}

	@Override
	public SnapshotObject readObject(SnapshotObject snapshot) {
		Query<SnapshotObject> query = createQuery();
		query.field(SystemObject.SYSTEM_ID).equal(snapshot.getSystemID());
		query.field(SnapshotObject.SNAPSHOT_ID).equal(snapshot.getSnapshotID());
		return findOne(query);
	}

	@Override
	public List<SnapshotObject> readObjects(List<SnapshotObject> snapshots) {
		List<SnapshotObject> readObjects = new LinkedList<SnapshotObject>();
		for (SnapshotObject snapshot : snapshots) {
			readObjects.add(readObject(snapshot));
		}
		return readObjects;
	}

	@Override
	public void writeObjects(List<SnapshotObject> snapshots) {
		for (SnapshotObject snapshot : snapshots) {
			writeObject(snapshot);
		}
	}

	@Override
	public void writeObject(SnapshotObject snapshot) {
		SystemDAO systemDAO = new SystemDAO(this.getDatastore());
		String systemID = snapshot.getSystemID();
		if (systemDAO.exists(systemID)) {
			this.save(snapshot);
		} else {
			throw new RuntimeException("Cannot write snapshot "
					+ snapshot.getSnapshotID() + " because system " + systemID
					+ " does not exist.");
		}
	}

	@Override
	public void deleteObjects(List<SnapshotObject> snapshots) {
		for (SnapshotObject snapshot : snapshots) {
			deleteObject(snapshot);
		}
	}

	@Override
	public void deleteObject(SnapshotObject snapshot) {
		delete(snapshot);

	}
}
