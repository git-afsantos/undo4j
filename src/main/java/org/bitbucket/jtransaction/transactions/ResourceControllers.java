package org.bitbucket.jtransaction.transactions;

import org.bitbucket.jtransaction.common.AccessMode;

final class ResourceControllers {
	private ResourceControllers() { throw new UnsupportedOperationException(); }


	/** */
	static ResourceController newController(AccessMode mode) {
		switch (mode) {
			case READ: return new ReadOnlyController();
			default: return new ResourceController();
		}
	}
}
