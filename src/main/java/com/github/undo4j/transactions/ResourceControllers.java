package com.github.undo4j.transactions;

import com.github.undo4j.common.AccessMode;

final class ResourceControllers {
	private ResourceControllers() {
		throw new UnsupportedOperationException();
	}

	/** */
	static ResourceController newController(AccessMode mode) {
		switch (mode) {
		case READ:
			return new ReadOnlyController();
		default:
			return new ResourceController();
		}
	}
}
