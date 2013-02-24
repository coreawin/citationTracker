package kr.co.topquadrant.citationTracker.co;

import java.io.File;

import com.tqk.ontobase.core.common.CoreException;
import com.tqk.ontobase.core.mvaule.KeyMultiValueManager;

/**
 * êµ¬ì? ??? ?°ì??°ë? lineë³?? êµ¬ë???? ???ë©?ê°??¼ì????¹ì? êµ¬ë???? ??? key??valueë¡?????´ì? ???.<br>
 * 
 * êµ¬ì? ??? ?°ì??°ë? key-multiVaule ???êµ¬ì¡°???????? ?´ë???<br>
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
	 *            ?°ì???????????ê²½ë? (???ëª??¬í?)
	 * @param delimeter
	 *            ?°ì??°í??¼ì? key,value êµ¬ë???
	 * @param repositoryPath
	 *            ë¦¬í?ì§??ë¦???????????ê²½ë?
	 * @param repositoryFileName
	 *            ë¦¬í?ì§??ë¦???? ??? ëª?
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
	 * key - multi value ??ª©???´ì???? B+Tree ?°ì??°ë? êµ¬ì????.
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
	 * Key????? ?¤ì? ê°?B+Treeë¥?êµ¬í???
	 * 
	 * @return
	 */
	public KeyMultiValueManager getKmvManager() {
		return kmvManager;
	}

}
