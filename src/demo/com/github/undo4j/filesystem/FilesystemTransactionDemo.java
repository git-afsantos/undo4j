package com.github.undo4j.filesystem;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;

import com.github.undo4j.resources.implementations.FilesystemInterface;
import com.github.undo4j.resources.implementations.FilesystemInternalResource;
import com.github.undo4j.resources.implementations.FilesystemResource;
import com.github.undo4j.transactions.ManagedResource;
import com.github.undo4j.transactions.TransactionManager;
import com.github.undo4j.transactions.TransactionManagers;

public class FilesystemTransactionDemo {

	private static File srcDir;
	private static File dstDir;
	private static File finalDir;

	public static void main(String[] args) throws Exception {
		setupDirectories();

		FilesystemInterface filesystemInterface = new FilesystemInterface();
		filesystemInterface.mv(srcDir, dstDir);
		filesystemInterface.cp(dstDir, finalDir);
		// filesystemInterface.rm(dstDir);

		pause();

		FilesystemTransactionDemo.doOperations(filesystemInterface);

		pause();

		cleanUp();
	}

	public static void doOperations(FilesystemInterface filesystemInterface) throws IOException, InterruptedException,
			ExecutionException {

		FilesystemOperationsTransaction transaction = new FilesystemOperationsTransaction(
				ManagedResource.from(new FilesystemResource(new FilesystemInternalResource())), filesystemInterface);

		TransactionManager tm = TransactionManagers.newSynchronousManager();
		Future<String> f = tm.submit(transaction);

		System.out.println(f.get() + "\n");

	}

	private static void setupSrcDirectory() throws IOException {
		srcDir = new File("/Users/mferreira/Desktop/demo/1-srcDir");
		srcDir.delete();
		if (!srcDir.mkdir()) {
			throw new IOException("could not create src directory " + srcDir.getAbsolutePath());
		}

		new File(srcDir, "cobol").createNewFile();
		new File(srcDir, "java").createNewFile();
		new File(srcDir, "prolog").createNewFile();
		new File(srcDir, "haskell").createNewFile();
		File subDir = new File(srcDir, "subDir");
		if (!subDir.mkdir()) {
			throw new IOException("could not create src sub directory " + srcDir.getAbsolutePath());
		}
		new File(subDir, "csharp").createNewFile();
		new File(subDir, "lisp").createNewFile();

		System.out.println("Source dir: " + srcDir.getAbsolutePath());
	}

	private static void setupDstDirectory() throws IOException {
		dstDir = new File("/Users/mferreira/Desktop/demo/2-dstDir");
		if (!dstDir.mkdir()) {
			throw new IOException("could not create dst directory " + srcDir.getAbsolutePath());
		}

		new File(dstDir, "apple").createNewFile();
		new File(dstDir, "orange").createNewFile();
		File subDir = new File(dstDir, "subDir");
		if (!subDir.mkdir()) {
			throw new IOException("could not create dst sub directory " + srcDir.getAbsolutePath());
		}
		new File(subDir, "mango").createNewFile();

		System.out.println("Destination dir: " + dstDir.getAbsolutePath());
	}

	private static void setupFinalDirectory() throws IOException {
		finalDir = new File("/Users/mferreira/Desktop/demo/3-finalDir");

		System.out.println("Final dir: " + finalDir.getAbsolutePath());
	}

	private static void setupDirectories() throws IOException {
		setupSrcDirectory();
		setupDstDirectory();
		setupFinalDirectory();
	}

	private static void pause() throws IOException {
		System.in.read();
	}

	private static void cleanUp() throws IOException {
		if (srcDir != null && srcDir.exists()) {
			FileUtils.deleteDirectory(srcDir);
		}
		if (dstDir != null && dstDir.exists()) {
			FileUtils.deleteDirectory(dstDir);
		}
		if (finalDir != null && finalDir.exists()) {
			FileUtils.deleteDirectory(finalDir);
		}

	}

}
