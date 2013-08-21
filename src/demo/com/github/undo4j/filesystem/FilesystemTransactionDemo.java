package com.github.undo4j.filesystem;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.github.undo4j.resources.implementations.FileOperation;
import com.github.undo4j.resources.implementations.FilesystemInterface;
import com.github.undo4j.resources.implementations.FilesystemInternalResource;
import com.github.undo4j.resources.implementations.FilesystemResource;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionManager;
import com.github.undo4j.transactions.TransactionManagers;

public class FilesystemTransactionDemo {

	private File srcDir;
	private File dstDir;

	public static void main(String[] args) throws Exception {
		FilesystemTransactionDemo demo = new FilesystemTransactionDemo();

		// pause execution to inspect directories
		System.in.read();

		demo.moveSrcToDst();
	}

	public FilesystemTransactionDemo() throws IOException {
		setupSrcDirectory();
		setupDstDirectory();
	}

	public void moveSrcToDst() throws IOException, InterruptedException, ExecutionException {
		FilesystemInterface filesystemInterface = new FilesystemInterface();

		MoveDirectoryTransaction transaction = new MoveDirectoryTransaction(srcDir, dstDir,
				ManagedResource.from(new FilesystemResource(new FilesystemInternalResource())), filesystemInterface);

		TransactionManager tm = TransactionManagers.newSynchronousManager();
		Future<String> f = tm.submit(transaction);

		System.out.println(f.get() + "\n");

	}

	public void setupSrcDirectory() throws IOException {
		srcDir = FileOperation.createTempDirectory("srcDir");
		srcDir.delete();
		if (!srcDir.mkdir()) {
			throw new IOException("could not create temporary src directory " + srcDir.getAbsolutePath());
		}

		File.createTempFile("aaa", "", srcDir);
		File.createTempFile("bbb", "", srcDir);
		File.createTempFile("ccc", "", srcDir);
		File.createTempFile("ddd", "", srcDir);
		File subDir = FileOperation.createTempDirectory("subDir", srcDir);
		File.createTempFile("eee", "", subDir);
		File.createTempFile("fff", "", subDir);

		System.out.println("Source dir: " + srcDir.getAbsolutePath());
	}

	public void setupDstDirectory() throws IOException {
		dstDir = FileOperation.createTempDirectory("dstDir");

		File.createTempFile("111", "", dstDir);
		File.createTempFile("222", "", dstDir);
		File.createTempFile("333", "", dstDir);
		File.createTempFile("444", "", dstDir);
		File subDir = FileOperation.createTempDirectory("subDir", dstDir);
		File.createTempFile("555", "", subDir);
		File.createTempFile("666", "", subDir);

		System.out.println("Destination dir: " + dstDir.getAbsolutePath());
	}

}
