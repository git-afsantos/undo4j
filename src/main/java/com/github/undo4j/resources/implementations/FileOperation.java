package com.github.undo4j.resources.implementations;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileOperation {

	protected enum Operation {
		COPY, MOVE, REMOVE;
	}

	protected Operation operation;
	protected File src;
	protected File dst;
	protected File tmpDir;
	protected File tmpSrc;
	protected File tmpDst;

	protected FileOperation(Operation operation, File src, File dst) throws IOException {
		this.operation = operation;
		this.src = src;
		this.dst = dst;
		this.tmpDir = createTempDirectory("fileOperation");
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
			removeSrcFile();
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

	protected void removeSrcFile() throws IOException {
		checkFileExists(src);
		saveCopyOfSource();
		src.delete();
	}

	protected void moveFile() throws IOException {
		copyFile();
		removeSrcFile();

		throw new IOException("simulated error!");
	}

	protected void copyFile() throws IOException {
		checkFileExists(src);
		saveCopyOfDestinationIfNecessary();
		deleteFileOrDirectory(dst);
		copyFileOrDirectory(src, dst);
	}

	protected void restoreSource() throws IOException {
		deleteFileOrDirectory(src);
		copyFileOrDirectory(tmpSrc, src);
	}

	protected void restoreDestination() throws IOException {
		deleteFileOrDirectory(dst);
		copyFileOrDirectory(tmpDst, dst);
	}

	protected void saveCopyOfSource() throws IOException {
		tmpSrc = new File(tmpDir, src.getName());
		copyFileOrDirectory(src, tmpSrc);
	}

	protected void saveCopyOfDestinationIfNecessary() throws IOException {
		if (dst.exists()) {
			tmpDst = new File(tmpDir, dst.getName());
			copyFileOrDirectory(dst, tmpDst);
		}
	}

	protected static void copyFileOrDirectory(File src, File dst) throws IOException {
		if (src.isDirectory()) {
			FileUtils.copyDirectory(src, dst);
		} else if (src.isFile()) {
			FileUtils.copyFile(src, dst);
		}
	}

	protected void deleteFileOrDirectory(File file) throws IOException {
		if (file.isDirectory()) {
			FileUtils.deleteDirectory(file);
		} else if (file.isFile()) {
			file.delete();
		}

	}

	protected static void checkFileExists(File file) throws IOException {
		if (!file.exists()) {
			throw new IOException("File " + file.getAbsolutePath() + " does not exist.");
		}
	}

	public static File createTempDirectory(String prefix) throws IOException {
		return createTempDirectory(prefix, null);
	}

	public static File createTempDirectory(String prefix, File directory) throws IOException {
		File tmpDir = File.createTempFile(prefix, Long.toString(System.nanoTime()), directory);
		tmpDir.delete();
		if (!tmpDir.mkdir()) {
			throw new IOException("could not create temporary directory " + tmpDir.getAbsolutePath());
		}
		return tmpDir;
	}

}
