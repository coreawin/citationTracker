package kr.co.topquadrant.citationTracker.co;

import java.io.File;

/**
 * B tree ??? ??? ?µì?.
 * 
 * @author coreawin
 * 
 */
public class RepositoryOption {

	public static final String workingDir = "." + File.separator + "tmp" + File.separator;

	protected static final int DIVISTION_SIZE = 1024 * 1024 * 1024;
	protected static final int COUNT_PER_SEGMENT = 64;
	protected static final int CACHE_SIZE = 1024 * 1024 * 64;
	protected static final int NODE_SIZE = 1024 * 4;
	protected static final int KEY_LENGTH = 8;
	protected static final int VALUE_LENGTH = 4;
	protected static final int NODE_LEG = 4;

	public static void initWorkingDir() throws Exception {
		delete(new File(workingDir));
	}

	public static final boolean delete(File file) {
		if (file.exists() == false)
			return true;

		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				if (delete(new File(file, children[i])) == false) {
					return false;
				}
			}
		}
		return file.delete();
	}

}
