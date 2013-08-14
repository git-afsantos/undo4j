package org.jtransaction.mongodb.dataaccess;

import org.jtransaction.mongodb.datamodel.System;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.query.Query;

public class SystemDAO extends BasicDAO<System, String> {

	public SystemDAO(Datastore datastore) {
		super(datastore);
	}

	public System read(String systemID) {
		Query<System> query = createQuery();
		query.field(System.SYSTEM_ID).equals(systemID);

		return findOne(query);
	}

	public void write(System system) {
		this.save(system);
	}
}
