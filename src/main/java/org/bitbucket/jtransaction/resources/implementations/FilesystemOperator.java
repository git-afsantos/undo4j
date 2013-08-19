package org.bitbucket.jtransaction.resources.implementations;

import java.io.File;

public interface FilesystemOperator {

	public void cp(File src, File dst);

	public void mv(File src, File dst);

	public void rm(File src);

}
