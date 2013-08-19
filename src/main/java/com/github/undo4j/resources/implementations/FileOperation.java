package com.github.undo4j.resources.implementations;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileOperation {

	public enum Operation {
		COPY, MOVE, REMOVE;
	}

	private Operation operation;
	private File src;
	private File dst;
	private File tmpDir;
	private File tmpSrc;
	private File tmpDst;

	protected FileOperation(Operation operation, File src, File dst)
			throws IOException {
		this.operation = operation;
		this.src = src;
		this.dst = dst;
		this.tmpDir = File.createTempFile("fileOperation",
				Long.toString(System.nanoTime()));
		if (!tmpDir.mkdir()) {
			throw new IOException("ould not create temporary directory "
					+ tmpDir.getAbsolutePath());
		}
	}

	public static FileOperation copyFile(File src, File dst) throws IOException {
		return new FileOperation(Operation.COPY, src, dst);
	}

	public static FileOperation moveFile(File src, File dst) throws IOException {
		return new FileOperation(Operation.MOVE, src, dst);
	}

	public static FileOperation removeFile(File src) throws IOException {
		return new FileOperation(Operation.REMOVE, src, null);
	}

	public void run() throws IOException {
		switch (operation) {
		case COPY:
			copyFile();
			break;
		case MOVE:
			moveFile();
			break;
		case REMOVE:
			removeFile();
			break;
		}
	}

	public void undo() throws IOException {
		switch (operation) {
		case COPY:
			restoreDestination();
			break;
		case MOVE:
			restoreSource();
			restoreDestination();
			break;
		case REMOVE:
			restoreSource();
			break;
		}
	}

	private void removeFile() throws IOException {
		checkFileExists(src);
		saveCopyOfSource();
		src.delete();
	}

	private void moveFile() throws IOException {
		copyFile();
		removeFile();
	}

	private void copyFile() throws IOException {
		checkFileExists(src);
		saveCopyOfDestinationIfNecessary();
		copyFileOrDirectory(src, dst);
	}

	private void restoreSource() throws IOException {
		copyFileOrDirectory(tmpSrc, src);
	}

	private void restoreDestination() throws IOException {
		copyFileOrDirectory(tmpDst, dst);
	}

	private void saveCopyOfSource() throws IOException {
		tmpSrc = new File(tmpDir, src.getName());
		copyFileOrDirectory(src, tmpSrc);
	}

	private void saveCopyOfDestinationIfNecessary() throws IOException {
		if (dst.exists()) {
			tmpDst = new File(tmpDir, dst.getName());
			copyFileOrDirectory(dst, tmpDst);
		}
	}

	private static void copyFileOrDirectory(File src, File dst)
			throws IOException {
		if (src.isDirectory()) {
			FileUtils.copyDirectory(src, dst);
		} else if (src.isFile()) {
			FileUtils.copyFile(src, dst);
		}
	}

	private static void checkFileExists(File file) throws IOException {
		if (!file.exists()) {
			throw new IOException("File " + file.getAbsolutePath()
					+ " does not exist.");
		}
	}

}
