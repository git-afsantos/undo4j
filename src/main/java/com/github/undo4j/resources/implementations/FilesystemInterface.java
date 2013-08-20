package com.github.undo4j.resources.implementations;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FilesystemInterface {
	protected List<FileOperation> operations = new LinkedList<>();

	public void rollback() throws IOException {
		for (int i = operations.size() - 1; i >= 0; i--) {
			FileOperation operation = operations.remove(i);
			operation.undo();
		}
	}

	public void cp(File src, File dst) throws IOException {
		FileOperation operation = FileOperation.copyFile(src, dst);
		go(operation);
	}

	public void mv(File src, File dst) throws IOException {
		FileOperation operation = FileOperation.moveFile(src, dst);
		go(operation);
	};

	public void rm(File src) throws IOException {
		FileOperation operation = FileOperation.removeFile(src);
		go(operation);
	}

	protected void go(FileOperation operation) throws IOException {
		operation.run();
		operations.add(operation);
	};
}
