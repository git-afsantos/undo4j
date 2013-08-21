package com.github.undo4j.filesystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.undo4j.resources.ImmutableState;
import com.github.undo4j.resources.implementations.FilesystemInterface;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionalCallable;

public class MoveDirectoryTransaction implements TransactionalCallable<String> {

	private ManagedResource<FilesystemInterface> filesystemResource;
	private FilesystemInterface filesystemInterface;

	public MoveDirectoryTransaction(File srcDirectory, File dstDirectory,
			ManagedResource<FilesystemInterface> filesystemResource, FilesystemInterface filesystemInterface)
			throws IOException {
		this.filesystemResource = filesystemResource;
		this.filesystemInterface = filesystemInterface;
		filesystemInterface.mv(srcDirectory, dstDirectory);
	}

	@Override
	public String call() throws Exception {
		filesystemResource.write(new ImmutableState<FilesystemInterface>(filesystemInterface));
		return "File system operatiosn complete. Transaction will now commit";
	}

	@Override
	public Iterable<ManagedResource<?>> getManagedResources() {
		List<ManagedResource<?>> list = new ArrayList<>(1);
		list.add(filesystemResource);
		return list;
	}

}
