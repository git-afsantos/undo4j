package com.github.undo4j.resources.implementations;

import com.github.undo4j.common.LockManager;
import com.github.undo4j.resources.InternalResource;
import com.github.undo4j.resources.StatelessResource;

public class FilesystemResource extends StatelessResource<FilesystemInterface> {

	public FilesystemResource(InternalResource<FilesystemInterface> resource,
			LockManager lockManager) {
		super(resource, lockManager);
	}

}
