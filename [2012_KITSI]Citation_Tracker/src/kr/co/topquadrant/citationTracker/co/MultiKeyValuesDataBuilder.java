package kr.co.topquadrant.citationTracker.co;

import java.io.File;

import com.tqk.ontobase.core.common.CoreException;
import com.tqk.ontobase.core.common.btree.BTreeVariableKeyNodeFactory;
import com.tqk.ontobase.core.common.btree.RangeSearchBTree;
import com.tqk.ontobase.core.util.TreeIterator;
import com.tqk.ontobase.core.util.array.ByteArrayUtil;

/**
 * ���� ���� Ƚ���� ���ϱ� ���� B+Tree ����<br>
 * 
 * @author neon
 * 
 */
public class MultiKeyValuesDataBuilder extends LoaderKMTreeRepository implements WrapTree {

	private RangeSearchBTree keyRangeSearchBTree;

	private String dataFilePath = "";

	/**
	 * ������
	 * 
	 * @param readFilePath
	 *            ������ ������ ������ (���ϸ� ����)
	 * @param delimeter
	 *            ������������ key,value ������
	 * @param repositoryPath
	 *            �������丮 ������ ������ ���
	 * @param repositoryFileName
	 *            �������丮 ���� ���� ��
	 * @param indexFileName
	 *            �ε��� ���� ���� ��
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
	 * �ش� Ű�� �����ϴ��� Ž���Ѵ�.
	 * 
	 * @param _key
	 * @return Ű�� �̹� �����ϸ� true�� �����Ѵ�.
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
	 * @return �ߺ� ���ŵ� Key Tree
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
	 * �߻� Ƚ���� �Է��Ѵ�.
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
