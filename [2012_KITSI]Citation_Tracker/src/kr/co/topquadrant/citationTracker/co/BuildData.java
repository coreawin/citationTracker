package kr.co.topquadrant.citationTracker.co;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FileUtils;

/**
 * �跮������ ���� ���絵 ������ �����Ѵ�.<br>
 * 
 * @author coreawin
 * 
 */
public class BuildData {

	final String FIELD_DELIMITER = ";";
	public static final String KEY_DELIMITER = "-";

	static final AtomicLong aData = new AtomicLong();

	/**
	 * ���絵 �м� Ÿ���� ����
	 * 
	 * @author coreawin
	 * @sinse 2012. 9. 26.
	 * @version 1.0
	 * @history 2012. 9. 26. : ���� �ۼ� <br>
	 * 
	 */
	OccurrenceMaker occurrenceMaker = null;
	MultiKeyValuesDataBuilder builder = null;

	/**
	 * ���絵 ������ ����� ��ġ.
	 */
	String repositoryPath = "";

	String editorName = null;

	public BuildData(String repositoryPath) throws Exception {
		this.repositoryPath = repositoryPath;
		this.editorName = editorName + "Measure";
		initRepository();
	}

	/**
	 * �۾��� ������ �ʱ�ȭ �Ѵ�.
	 * 
	 * @throws Exception
	 */
	private void initRepository() throws Exception {
		deleteAllResourceFile(repositoryPath);
		FileUtil.mkdirs(repositoryPath);
		builder = dataLoader();
	}

	/**
	 * �ش� ���丮�� ��� ������ �����.
	 * 
	 * @param path
	 */
	private void deleteAllResourceFile(String path) {
		try {
			FileUtils.forceDelete(new File(path));
		} catch (Exception e) {
		}
	}

	public void buildOccurrenceData(Map<String, String> datas) throws Exception {
		for (String eid : datas.keySet()) {
			String rceid = datas.get(eid);
			String key = rceid + KEY_DELIMITER + eid;
			builder.addData(key, eid);
		}
	}

	public void buildOccurrenceData(String eid, String rceid) throws Exception {
		builder.addData(eid, rceid);
	}

	private MultiKeyValuesDataBuilder dataLoader() throws Exception {
		return new MultiKeyValuesDataBuilder(true, repositoryPath, "rep", "idx");
	}
	
	public Map<String, Integer> getOccurrenceData(){
		OccurrenceMaker maker = createSimilarityRepository();
		return maker.buildDataCoOccurrence(builder);
	}
	
	public void flush(){
		if (builder != null) {
			builder.flush();
		}
	}

	public void close() {
		if (occurrenceMaker != null) {
			occurrenceMaker.close();
		}
		if (builder != null) {
			builder.closeResource();
		}
	}

	/**
	 * ���絵 ���̺��� ������ �������͸��� �����Ѵ�.<br>
	 * ������ �̹� �����Ѵٸ� close���� �ʴ� �������͸� ������ �����´�.<br>
	 * 
	 * @param seq
	 *            Ŭ������ �м� SEQ
	 * @param dataType
	 *            REFERENCE, CITATION, KC ����
	 * @return
	 */
	protected OccurrenceMaker createSimilarityRepository() {
		return new OccurrenceMaker(repositoryPath, "similarity");
	}

	protected OccurrenceMaker getSimilarityRepository() {
		return new OccurrenceMaker(false, repositoryPath, "similarity");
	}

}
