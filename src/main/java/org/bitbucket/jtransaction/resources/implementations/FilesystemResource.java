package org.bitbucket.jtransaction.resources.implementations;

import org.bitbucket.jtransaction.common.LockManager;
import org.bitbucket.jtransaction.resources.InternalResource;
import org.bitbucket.jtransaction.resources.StatelessResource;

public class FilesystemResource extends StatelessResource<FilesystemInterface> {

	public FilesystemResource(InternalResource<FilesystemInterface> resource,
			LockManager lockManager) {
		super(resource, lockManager);
	}

}
