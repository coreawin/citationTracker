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
 * 유사도를 구한다.
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
	 * 입력된 두개의 set를 비교하여 같은 데이터가 들어온 개수를 센다.<br>
	 * 
	 * @param a
	 *            Set
	 * @param b
	 *            Set
	 * @return a set과 b set이 같이 가지고 있는 데이터 수.
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
	 * 파일 자료구조에 입력한다.<br>
	 * 
	 * @param rsb
	 *            RangeSearchBTree
	 * @param key
	 *            btree에 입력할 key
	 * @param value
	 *            btree에 입력할 value
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
	 * 입력된 documents를 기반으로 유사도를 계산한 후에 해당하는 repositoryName의 파일로 유사도 정보를 저장한다. <br>
	 * 유사도를 계산할 데이터 셋에는 한번에 유사도를 계산할 모든 데이터가 있어야 한다. <br>
	 * 추후에 또다른 데이터 셋으로 이전 데이터 셋에 포함된 값이 들어오게 되면 이전 데이터의 유사도가 유지되며, <br>
	 * 새로 들어온 데이터는 유사도 계산이 저장되지 않는다.<br>
	 * 
	 * @param rp
	 *            유사도 정보를 기록할 파일의 저장 경로
	 * @param rn
	 *            유사도 정보를 기록할 파일 이름.
	 * @param dataSet
	 *            유사도를 계산할 데이터 정보.
	 * @return 리파지터리 파일 명..
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
					// x==y라면 skip
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
					ConsoleOut.getInsance().info("Co-occurrence 분석 진행 : " + (cnt - 1) + " / " + totalSize);
				}
				cnt++;
				skipCount++;
			}
			ConsoleOut.getInsance().info("Co-occurrence 분석 완료 " + cnt);
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
		double d = 100.0; /* 유사도 값이 0과 1사이의 값이므로 1보다 큰 값중 가장 작은 정수이면 된다. */
		byte[] key = ByteArrayUtil.stringToByte(k);
		byte[] value = rsb.find(key, 0, key.length);
		if (value == null)
			return d;
		String s = ByteArrayUtil.readString(value, 0, value.length);
		return Double.parseDouble(s);
	}

	/**
	 * DataLoader 의 데이터 구축 갯수를 구한다.<br>
	 * 
	 * @param l
	 *            클러스터 분석 대상 데이터.
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
	 * 단어에 따른 유사도 값이 저장된 Btree의 인스턴스를 리턴한다.
	 * 
	 * @return
	 * @throws CoreException
	 */
	public RangeSearchBTree getRangeSearch() throws CoreException {
		return rsb;
	}


}
