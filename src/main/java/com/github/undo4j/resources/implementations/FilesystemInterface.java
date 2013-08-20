package com.github.undo4j.resources.implementations;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FilesystemInterface {
	protected List<FileOperation> operationsToExecute = new LinkedList<>();
	protected List<FileOperation> executedOperations = new LinkedList<>();

	public void rollback() throws IOException {
		for (int i = executedOperations.size() - 1; i >= 0; i--) {
			FileOperation operation = executedOperations.remove(i);
			operation.undo();
		}
		executedOperations = new LinkedList<>();
	}

	public void cp(File src, File dst) throws IOException {
		operationsToExecute.add(FileOperation.copyFile(src, dst));
	}

	public void mv(File src, File dst) throws IOException {
		operationsToExecute.add(FileOperation.moveFile(src, dst));
	}

	public void rm(File src) throws IOException {
		operationsToExecute.add(FileOperation.removeFile(src));
	}

	public void run() throws IOException {
		for (FileOperation operation : operationsToExecute) {
			executedOperations.add(operation);
			operation.run();
		}
		operationsToExecute = new LinkedList<>();
	}
}
