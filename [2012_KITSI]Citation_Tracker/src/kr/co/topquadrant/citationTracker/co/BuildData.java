package kr.co.topquadrant.citationTracker.co;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FileUtils;

/**
 * 계량정보를 위한 유사도 정보를 구축한다.<br>
 * 
 * @author coreawin
 * 
 */
public class BuildData {

	final String FIELD_DELIMITER = ";";
	public static final String KEY_DELIMITER = "-";

	static final AtomicLong aData = new AtomicLong();

	/**
	 * 유사도 분석 타입을 정의
	 * 
	 * @author coreawin
	 * @sinse 2012. 9. 26.
	 * @version 1.0
	 * @history 2012. 9. 26. : 최초 작성 <br>
	 * 
	 */
	OccurrenceMaker occurrenceMaker = null;
	MultiKeyValuesDataBuilder builder = null;

	/**
	 * 유사도 정보가 저장될 위치.
	 */
	String repositoryPath = "";

	String editorName = null;

	public BuildData(String repositoryPath) throws Exception {
		this.repositoryPath = repositoryPath;
		this.editorName = editorName + "Measure";
		initRepository();
	}

	/**
	 * 작업할 공간을 초기화 한다.
	 * 
	 * @throws Exception
	 */
	private void initRepository() throws Exception {
		deleteAllResourceFile(repositoryPath);
		FileUtil.mkdirs(repositoryPath);
		builder = dataLoader();
	}

	/**
	 * 해당 디렉토리의 모든 파일을 지운다.
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
	 * 유사도 테이블을 저장할 리파지터리를 생성한다.<br>
	 * 기존에 이미 존재한다면 close되지 않는 리파지터리 정보를 가져온다.<br>
	 * 
	 * @param seq
	 *            클러스터 분석 SEQ
	 * @param dataType
	 *            REFERENCE, CITATION, KC 정보
	 * @return
	 */
	protected OccurrenceMaker createSimilarityRepository() {
		return new OccurrenceMaker(repositoryPath, "similarity");
	}

	protected OccurrenceMaker getSimilarityRepository() {
		return new OccurrenceMaker(false, repositoryPath, "similarity");
	}

}
