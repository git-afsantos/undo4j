package org.jtransaction.mongodb.dataaccess;

import org.jtransaction.mongodb.datamodel.Snapshot;
import org.jtransaction.mongodb.datamodel.System;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;

public class SnapshotDAO extends BasicDAO<Snapshot, String> {

	public SnapshotDAO(Datastore datastore) {
		super(datastore);
	}

	public Snapshot read(String systemID, String snapshotID) {
		Query<Snapshot> query = createQuery();
		query.field(System.SYSTEM_ID).equals(systemID);
		query.field(Snapshot.SNAPSHOT_ID).equals(snapshotID);
		return findOne(query);
	}

	public void write(Snapshot snapshot) {
		this.save(snapshot);
	}

}
