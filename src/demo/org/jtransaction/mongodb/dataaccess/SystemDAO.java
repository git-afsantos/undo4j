package org.jtransaction.mongodb.dataaccess;

import java.util.LinkedList;
import java.util.List;

import org.bitbucket.jtransaction.resources.implementations.MongoDAO;
import org.jtransaction.mongodb.datamodel.SnapshotObject;
import org.jtransaction.mongodb.datamodel.SystemObject;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;

public class SystemDAO extends BasicDAO<SystemObject, String> implements
		MongoDAO<SystemObject> {

	public SystemDAO(Datastore datastore) {
		super(datastore);
	}

	@Override
	public SystemObject readObject(SystemObject system) {
		Query<SystemObject> query = buildFetchSystemQuery(system.getSystemID());

		return findOne(query);
	}

	@Override
	public List<SystemObject> readObjects(List<SystemObject> systems) {
		List<SystemObject> readSystems = new LinkedList<SystemObject>();
		for (SystemObject system : systems) {
			readSystems.add(readObject(system));
		}
		return systems;
	}

	@Override
	public void writeObject(SystemObject system) {
		if (!exists(system.getSystemID())) {
			this.save(system);
		}
	}

	@Override
	public void writeObjects(List<SystemObject> systems) {
		for (SystemObject system : systems) {
			writeObject(system);
		}
	}

	@Override
	public void deleteObject(SystemObject system) {
		SnapshotDAO snapshotDAO = new SnapshotDAO(this.getDatastore());
		String systemID = system.getSystemID();
		if (!snapshotDAO.exists(new SnapshotObject(systemID, null))) {
			delete(readObject(system));
		} else {
			throw new RuntimeException("Cannot delete system " + systemID
					+ " because it has snapshots.");
		}

	}

	@Override
	public void deleteObjects(List<SystemObject> systems) {
		for (SystemObject system : systems) {
			deleteObject(system);
		}
	}

	public boolean exists(String systemID) {
		return exists(buildFetchSystemQuery(systemID));
	}

	private Query<SystemObject> buildFetchSystemQuery(String systemID) {
		Query<SystemObject> query = createQuery();
		query.field(SystemObject.SYSTEM_ID).equal(systemID);
		return query;
	}

}
