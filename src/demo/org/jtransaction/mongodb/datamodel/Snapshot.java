package org.jtransaction.mongodb.datamodel;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Property;

@Entity(value = "snapshot")
public class Snapshot {

	public static final String SNAPSHOT_ID = "snapshotID";

	@Property(System.SYSTEM_ID)
	private String systemID;

	@Property(SNAPSHOT_ID)
	private String snapshotID;

	public String getSystemID() {
		return systemID;
	}

	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}

	public String getSnapshotID() {
		return snapshotID;
	}

	public void setSnapshotID(String snapshotID) {
		this.snapshotID = snapshotID;
	}

}
