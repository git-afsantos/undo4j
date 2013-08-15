package org.jtransaction.mongodb.datamodel;

import org.bson.types.ObjectId;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Id;
import com.github.jmkgreen.morphia.annotations.Property;

@Entity(value = "systems", noClassnameStored = true)
public class SystemObject {

	public static final String SYSTEM_ID = "systemID";

	@Id
	@Property("id")
	private ObjectId id;

	@Property(SYSTEM_ID)
	private String systemID;

	public SystemObject() {
	}

	public SystemObject(String systemID) {
		this.systemID = systemID;
	}

	public String getSystemID() {
		return systemID;
	}

	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "System [id=" + id + ", systemID=" + systemID + "]";
	}

}
