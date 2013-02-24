package kr.co.topquadrant.citationTracker.co;

import java.io.File;

import com.tqk.ontobase.core.common.CoreException;
import com.tqk.ontobase.core.mvaule.KeyMultiValueManager;

/**
 * 구�? ??? ?��??��? line�?? 구�???? ???�?�??��????��? 구�???? ??? key??value�?????��? ???.<br>
 * 
 * 구�? ??? ?��??��? key-multiVaule ???구조???????? ?��???<br>
 * 
 * @author neon
 * 
 */
public abstract class LoaderKMTreeRepository {

	private KeyMultiValueManager kmvManager;

	/**
	 * ?????
	 * 
	 * @param readFilePath
	 *            ?��???????????경�? (???�??��?)
	 * @param delimeter
	 *            ?��??��??��? key,value 구�???
	 * @param repositoryPath
	 *            리�?�??�???????????경�?
	 * @param repositoryFileName
	 *            리�?�??�???? ??? �?
	 */
	public LoaderKMTreeRepository(boolean iscreate, String repositoryPath, String repositoryFileName) {
		// this.readFilePath = readFilePath;
		// this.repositoryPath = repositoryPath;
		// this.repositoryFileName = repositoryFileName;
		try {
			kmvManager = new KeyMultiValueManager(iscreate, repositoryPath + File.separator + repositoryFileName,
					RepositoryOption.DIVISTION_SIZE, RepositoryOption.COUNT_PER_SEGMENT, RepositoryOption.CACHE_SIZE,
					RepositoryOption.NODE_SIZE, RepositoryOption.KEY_LENGTH, RepositoryOption.VALUE_LENGTH);
			if (iscreate)
				kmvManager.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * key - multi value ??��???��???? B+Tree ?��??��? 구�????.
	 * 
	 * @throws Exception
	 */
	public void addData(String k, String v) throws Exception {
		k = k.trim();
		v = v.trim();
		if ("".equals(k))
			return;
		if ("".equals(v))
			return;
		kmvManager.add(k, v);
		additionLoad(k, v);
	}

	abstract public void additionLoad(String k, String v) throws Exception;

	public void close() {
		try {
			flush();
			kmvManager.close();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public void flush() {
		try {
			kmvManager.flush();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Key????? ?��? �?B+Tree�?구�???
	 * 
	 * @return
	 */
	public KeyMultiValueManager getKmvManager() {
		return kmvManager;
	}

}
