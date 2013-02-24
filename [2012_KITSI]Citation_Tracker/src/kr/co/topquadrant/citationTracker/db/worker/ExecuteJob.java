/**
 * 
 */
package kr.co.topquadrant.citationTracker.db.worker;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.co.topquadrant.citationTracker.CSVReader;
import kr.co.topquadrant.citationTracker.ExcelReader;
import kr.co.topquadrant.citationTracker.ResultWriter;
import kr.co.topquadrant.citationTracker.console.ConsoleOut;
import kr.co.topquadrant.citationTracker.data.ParameterInfo;
import kr.co.topquadrant.citationTracker.db.ConnectionFactory;

/**
 * @author coreawin
 * @sinse 2012. 11. 30.
 * @version 1.0
 * @history 2012. 11. 30. : 최초 작성 <br>
 * 
 */
public abstract class ExecuteJob {
	
	protected final String DUMMY = "-1";

	protected List<List<String>> datas = null;
	protected ParameterInfo parameter = null;
	protected ConnectionFactory cf = ConnectionFactory.getInstance();
	protected Connection conn = null;
	protected PreparedStatement psmt = null;
	protected ResultSet rs = null;
	protected ResultWriter writer = null;
	protected Set<String> inputEidSets = new LinkedHashSet<String>();
	protected boolean inside = false;
	private String filePath;

	public ExecuteJob(ParameterInfo p) throws Exception {
		this.parameter = p;
	}

	private void readDataFile() throws Exception {
		try {
			ConsoleOut.getInsance().info("파일로부터 분석 대상 데이터를 수집하고 있습니다.");
			
			if(this.parameter.getInputFilePath().endsWith(".csv")){
				CSVReader cr = new CSVReader(this.parameter.getInputFilePath());
				this.datas = cr.getIDData();
			}else{
				ExcelReader er = new ExcelReader(this.parameter.getInputFilePath());
				this.datas = er.getIDData();
				ConsoleOut.getInsance().info("분석대상 데이터 수집을 완료하였습니다.");
			}
		} catch (Exception e) {
			throw e;
		} finally{
		}
	}

	private void setupWriter() throws IOException {
		String date = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
		String writerFileName = date + "_" + parameter.getExeCutetype() + ".txt";
		ConsoleOut.getInsance().info("결과로 생성될 파일명은 [" + writerFileName + "] 입니다.");
		filePath = "./" + writerFileName;
		writer = new ResultWriter(filePath);
	}

	private void init() throws Exception {
		readDataFile();
		setupWriter();
		switch (parameter.getTrackerType()) {
		case Inside:
			inside = true;
			setupAllEIDSet();
			break;
		}
	}

	protected void setupAllEIDSet() {
		for (List<String> list : datas) {
			for (String eid : list) {
				inputEidSets.add(eid.trim());
			}
		}
	}

	public void execute() throws Exception {
		if (parameter == null) {
			throw new NullPointerException("분석하기 위한 파라미터 정보가 누락되었습니다.");
		}
		System.out.println("startYear " + parameter.getStartYear());
		System.out.println("endYear " + parameter.getEndYear());
		init();
		try{
			if(this.datas.size() > 0){
				switch (parameter.getDirectionType()) {
				case Backward:
					exeBackward();
					break;
				case Forward:
					exeForward();
					break;
				default:
					ConsoleOut.getInsance().info("Citation Type에 대한 실행 옵션이 아닙니다.");
					break;
				}
			}else{
				ConsoleOut.getInsance().info("수집된 분석 대상 데이터가 존재하지 않습니다. 입력파일을 확인해 주세요.");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close();
		}
	}

	protected abstract void exeForward() throws Exception;

	protected abstract void exeBackward() throws Exception;

	public abstract void closeResource();

	/**
	 * Citation Tracker 파일의 경로를 받아온다.
	 * 
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}

	private void close() {
		closeResource();
		if (writer != null) {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected List<Map<String, Integer>> convertData(Map<String, Integer> result) {
		int count = 0;
		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
		Map<String, Integer> tmp = new HashMap<String, Integer>();
		for (String k : result.keySet()) {
			int value = result.get(k);
			tmp.put(k, value);
			if (count > 500) {
				list.add(tmp);
				tmp = new HashMap<String, Integer>();
				count = 0;
			}
			count++;
		}
		if (tmp.size() > 0) {
			list.add(tmp);
		}
		return list;
	}
	
	protected List<Set<String>> convertData(Set<String> sets) {
		int count = 0;
		List<Set<String>> list = new LinkedList<Set<String>>();
		Set<String> tmp = new HashSet<String>();
		for (String k : sets) {
			tmp.add(k);
			if (count > 500) {
				list.add(tmp);
				tmp = new HashSet<String>();
				count = 0;
			}
			count++;
		}
		if (tmp.size() > 0) {
			list.add(tmp);
		}
		return list;
	}
}
