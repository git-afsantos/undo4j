package org.jtransaction.mongodb.datamodel;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Property;

@Entity(value = "snapshots", noClassnameStored = true)
public class SnapshotObject {

	public static final String SNAPSHOT_ID = "snapshotID";

	@Id
	@Property("id")
	private ObjectId id;

	@Property(SystemObject.SYSTEM_ID)
	private String systemID;

	@Property(SNAPSHOT_ID)
	private String snapshotID;

	public SnapshotObject() {
	}

	public SnapshotObject(String systemID, String snapshotID) {
		this.systemID = systemID;
		this.snapshotID = snapshotID;
	}

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

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Snapshot [id=" + id + ", systemID=" + systemID
				+ ", snapshotID=" + snapshotID + "]";
	}

}
