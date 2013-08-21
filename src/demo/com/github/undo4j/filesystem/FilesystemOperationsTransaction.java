package com.github.undo4j.filesystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.resources.implementations.FilesystemInterface;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionalCallable;

public class FilesystemOperationsTransaction implements TransactionalCallable<String> {

	private ManagedResource<FilesystemInterface> filesystemResource;
	private FilesystemInterface filesystemInterface;

	public FilesystemOperationsTransaction(ManagedResource<FilesystemInterface> filesystemResource,
			FilesystemInterface filesystemInterface) throws IOException {
		this.filesystemResource = filesystemResource;
		this.filesystemInterface = filesystemInterface;
	}

	@Override
	public String call() throws Exception {
		filesystemResource.write(new ImmutableState<FilesystemInterface>(filesystemInterface));

		return "File system operations complete. Transaction will now commit";
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> list = new ArrayList<>(1);
		list.add(filesystemResource);
		return list;
	}

}
