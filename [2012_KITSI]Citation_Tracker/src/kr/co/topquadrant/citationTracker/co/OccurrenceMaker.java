package kr.co.topquadrant.citationTracker.co;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import kr.co.topquadrant.citationTracker.console.ConsoleOut;

import com.tqk.ontobase.core.common.CoreException;
import com.tqk.ontobase.core.common.btree.BTreeVariableNodeFactory;
import com.tqk.ontobase.core.common.btree.RangeSearchBTree;
import com.tqk.ontobase.core.mvaule.KeyMultiValueManager;
import com.tqk.ontobase.core.util.TreeIterator;
import com.tqk.ontobase.core.util.array.ByteArrayUtil;

/**
 * ���絵�� ���Ѵ�.
 * 
 * @author neon
 * 
 */
public class OccurrenceMaker {

	RangeSearchBTree rsb = null;
	String rsbName = "";

	/**
	 * Repository Path
	 */
	String rp;
	/**
	 * Repository Name
	 */
	String rn;

	public OccurrenceMaker(String repositoryPath, String repositoryName) {
		this(true, repositoryPath, repositoryName);
	}

	public OccurrenceMaker(boolean isCreate, String repositoryPath, String repositoryName) {
		this.rp = repositoryPath;
		this.rn = repositoryName;
		rsbName = rp + File.separator + rn + "_rb.tree";
		try {
			rsb = new RangeSearchBTree(isCreate, rsbName, RepositoryOption.DIVISTION_SIZE, RepositoryOption.CACHE_SIZE,
					new BTreeVariableNodeFactory(RepositoryOption.NODE_SIZE));
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void flush() {
		try {
			if (rsb != null) {
				rsb.flush();
			}
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	public void close() {
		try {
			if (rsb != null) {
				rsb.flush();
				rsb.close();
			}
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * �Էµ� �ΰ��� set�� ���Ͽ� ���� �����Ͱ� ���� ������ ����.<br>
	 * 
	 * @param a
	 *            Set
	 * @param b
	 *            Set
	 * @return a set�� b set�� ���� ������ �ִ� ������ ��.
	 */
	Set<String> duplicationCheckSet = new HashSet<String>();

	private int duplicationDataCount(String[] a, String[] b) {
		duplicationCheckSet.clear();
		for (String aa : a) {
			duplicationCheckSet.add(aa);
		}
		int w = 0;
		for (String bb : b) {
			if (duplicationCheckSet.contains(bb)) {
				w++;
			}
		}
		return w;
	}

	/**
	 * ���� �ڷᱸ���� �Է��Ѵ�.<br>
	 * 
	 * @param rsb
	 *            RangeSearchBTree
	 * @param key
	 *            btree�� �Է��� key
	 * @param value
	 *            btree�� �Է��� value
	 * @throws CoreException
	 */
	private void insertRangeSearchBtree(String key, String value) throws CoreException {
		byte[] _key = ByteArrayUtil.stringToByte(key);
		byte[] _value = ByteArrayUtil.stringToByte(value);
		rsb.checkInsert(_key, 0, _key.length, _value, 0, _value.length);
	}

	private void deleteRangeSearchBtree(String key) throws CoreException {
		byte[] _key = ByteArrayUtil.stringToByte(key);
		rsb.delete(_key, 0, _key.length);
	}

	/**
	 * �Էµ� documents�� ������� ���絵�� ����� �Ŀ� �ش��ϴ� repositoryName�� ���Ϸ� ���絵 ������ �����Ѵ�. <br>
	 * ���絵�� ����� ������ �¿��� �ѹ��� ���絵�� ����� ��� �����Ͱ� �־�� �Ѵ�. <br>
	 * ���Ŀ� �Ǵٸ� ������ ������ ���� ������ �¿� ���Ե� ���� ������ �Ǹ� ���� �������� ���絵�� �����Ǹ�, <br>
	 * ���� ���� �����ʹ� ���絵 ����� ������� �ʴ´�.<br>
	 * 
	 * @param rp
	 *            ���絵 ������ ����� ������ ���� ���
	 * @param rn
	 *            ���絵 ������ ����� ���� �̸�.
	 * @param dataSet
	 *            ���絵�� ����� ������ ����.
	 * @return �������͸� ���� ��..
	 */
	private int totalSize = 0;

	private Map<String, Integer> buildData(MultiKeyValuesDataBuilder loader) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		try {
			int cnt = 0;
			totalSize = countData(loader) * 2;
			Iterator<String> xIter = loader.getIterator();
			KeyMultiValueManager km = loader.getKmvManager();
			while (xIter.hasNext()) {
				String x = (String) xIter.next();
				int skipCount = 0;
				Iterator<String> yIter = loader.getIterator();
				while (yIter.hasNext()) {
					// x==y��� skip
					String y = (String) yIter.next();
					if (skipCount < cnt) {
						skipCount++;
						continue;
					}
					String[] xv = km.getVaule(x);
					String[] yv = km.getVaule(y);
					int sameValue = duplicationDataCount(xv, yv);
					if (sameValue != 0) {
						result.put(x.trim() + BuildData.KEY_DELIMITER + y.trim(), sameValue);
					}
				}
				if (cnt % 1000 == 0) {
					ConsoleOut.getInsance().info("Co-occurrence �м� ���� : " + (cnt - 1) + " / " + totalSize);
				}
				cnt++;
				skipCount++;
			}
			ConsoleOut.getInsance().info("Co-occurrence �м� �Ϸ� " + cnt);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally{
			
		}
		return result;
	}
	
	/**
	 * @param builder
	 * @return
	 */
	public Map<String, Integer> buildDataCoOccurrence(MultiKeyValuesDataBuilder builder) {
		return buildData(builder);
	}

	private double findKey(String k) throws CoreException {
		double d = 100.0; /* ���絵 ���� 0�� 1������ ���̹Ƿ� 1���� ū ���� ���� ���� �����̸� �ȴ�. */
		byte[] key = ByteArrayUtil.stringToByte(k);
		byte[] value = rsb.find(key, 0, key.length);
		if (value == null)
			return d;
		String s = ByteArrayUtil.readString(value, 0, value.length);
		return Double.parseDouble(s);
	}

	/**
	 * DataLoader �� ������ ���� ������ ���Ѵ�.<br>
	 * 
	 * @param l
	 *            Ŭ������ �м� ��� ������.
	 * @return
	 */
	private int countData(MultiKeyValuesDataBuilder l) {
		int cnt = 0;
		Iterator<String> i = l.getIterator();
		while (i.hasNext()) {
			i.next();
			cnt++;
		}
		return cnt;
	}

	/**
	 * �ܾ ���� ���絵 ���� ����� Btree�� �ν��Ͻ��� �����Ѵ�.
	 * 
	 * @return
	 * @throws CoreException
	 */
	public RangeSearchBTree getRangeSearch() throws CoreException {
		return rsb;
	}


}
