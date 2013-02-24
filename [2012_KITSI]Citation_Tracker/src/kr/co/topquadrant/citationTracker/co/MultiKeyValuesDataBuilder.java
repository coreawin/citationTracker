package kr.co.topquadrant.citationTracker.co;

import java.io.File;

import com.tqk.ontobase.core.common.CoreException;
import com.tqk.ontobase.core.common.btree.BTreeVariableKeyNodeFactory;
import com.tqk.ontobase.core.common.btree.RangeSearchBTree;
import com.tqk.ontobase.core.util.TreeIterator;
import com.tqk.ontobase.core.util.array.ByteArrayUtil;

/**
 * 동시 출현 횟수를 구하기 위한 B+Tree 구축<br>
 * 
 * @author neon
 * 
 */
public class MultiKeyValuesDataBuilder extends LoaderKMTreeRepository implements WrapTree {

	private RangeSearchBTree keyRangeSearchBTree;

	private String dataFilePath = "";

	/**
	 * 생성자
	 * 
	 * @param readFilePath
	 *            데이터 파일의 절대경로 (파일명 포함)
	 * @param delimeter
	 *            데이터파일의 key,value 구분자
	 * @param repositoryPath
	 *            리파지토리 파일이 생성될 경로
	 * @param repositoryFileName
	 *            리파지토리 생성 파일 명
	 * @param indexFileName
	 *            인덱스 생성 파일 명
	 */
	public MultiKeyValuesDataBuilder(boolean iscreate, String repositoryPath, String repositoryFileName, String indexFileName) {
		super(iscreate, repositoryPath, repositoryFileName);
		try {
			dataFilePath = repositoryPath + File.separator + indexFileName;
			keyRangeSearchBTree = new RangeSearchBTree(iscreate, dataFilePath, RepositoryOption.DIVISTION_SIZE,
					RepositoryOption.CACHE_SIZE, new BTreeVariableKeyNodeFactory(RepositoryOption.NODE_SIZE, 1));
			if (iscreate) {
				keyRangeSearchBTree.clear();
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 해당 키가 존재하는지 탐색한다.
	 * 
	 * @param _key
	 * @return 키가 이미 존재하면 true를 리턴한다.
	 * @throws CoreException
	 */
	public boolean exist(String _key) throws CoreException {
		byte[] key = ByteArrayUtil.stringToByte(_key);
		return (keyRangeSearchBTree.exist(key, 0, key.length));
	}

	@Override
	public void closeResource() {
		try {
			close();
			keyRangeSearchBTree.close();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void flush() {
		try {
			keyRangeSearchBTree.flush();
			super.flush();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return 중복 제거된 Key Tree
	 */
	public RangeSearchBTree getKeyTree() {
		return keyRangeSearchBTree;
	}

	public TreeIterator<String> getIterator() {
		return new TreeIterator<String>(keyRangeSearchBTree);
	}

	@Override
	public void additionLoad(String k, String v) throws Exception {
		if (keyRangeSearchBTree != null) {
			byte[] data = ByteArrayUtil.stringToByte(k);
			byte[] value = new byte[] { 0 };
			keyRangeSearchBTree.checkInsert(data, 0, data.length, value, 0, 1);
		}
	}

	/**
	 * 발생 횟수를 입력한다.
	 * 
	 * @param preK
	 * @param postK
	 * @throws CoreException
	 */
	final static byte[] value = new byte[] { 0 };

	public void loadCount(String preK, String postK, String v) throws Exception {
		if (keyRangeSearchBTree != null) {
			String key = preK + ":" + postK;
			String key2 = postK + ":" + preK;
			getKmvManager().add(key, v);
			getKmvManager().add(key2, v);
		}
	}

}
