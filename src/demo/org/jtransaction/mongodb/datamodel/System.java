package org.jtransaction.mongodb.datamodel;

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Property;

@Entity(value = "system")
public class System {

	public static final String SYSTEM_ID = "systemID";

	@Property(SYSTEM_ID)
	private String systemID;

	public String getSystemID() {
		return systemID;
	}

	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}

}
