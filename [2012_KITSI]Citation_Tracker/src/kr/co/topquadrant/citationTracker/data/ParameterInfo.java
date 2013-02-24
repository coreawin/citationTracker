package kr.co.topquadrant.citationTracker.data;

import kr.co.topquadrant.citationTracker.ui.ClusterTrackerMainUI.CitationDirection;
import kr.co.topquadrant.citationTracker.ui.ClusterTrackerMainUI.CitationTrackerOption;
import kr.co.topquadrant.citationTracker.ui.ClusterTrackerMainUI.ExecuteType;

import org.eclipse.jface.text.TextViewer;

/**
 * CitationTracker 실행하기 위해 설정하는 파라미터 정보이다.
 * 
 * @author 이관재
 * @sinse 2012. 11. 27.
 * @version 1.0
 * @history 2012. 11. 27. : 최초작성
 */
public class ParameterInfo {

	/**
	 * Citation Direction 정보
	 * 
	 * Forward 또는 Backward
	 * 
	 * directionType
	 */
	private CitationDirection directionType;
	/**
	 * CitationTracker Option 정보
	 * 
	 * Inside or Outside
	 * 
	 * trackerType
	 */
	private CitationTrackerOption option;

	/**
	 * 연도 정보(반영을 하지 않을 경우에는 시작 연도와 끝연도는 -1이다.)
	 */
	private int startYear;
	private int endYear;

	/**
	 * Excel File이 있는 경로이다, excelFilePath
	 */
	private String excelFilePath;

	/**
	 * 
	 * 시작 실행 타입을 정보이다.
	 * 
	 * exeCutetype
	 */
	private ExecuteType executeType;

	/**
	 * LogConsole를 찍는 컴포넌트 이다. logConsole
	 */
	private TextViewer logConsole;
	private String citationTrackerName;

	/**
	 * 
	 * 
	 * @param directionType
	 *            Citation Direction 정보
	 * 
	 * @param trackerType
	 *            CitationTracker Option 정보
	 * 
	 * @param startYear
	 *            시작 연도 정보(반영을 하지 않을 경우에는 시작 연도는 -1이다.)
	 * 
	 * @param endYear
	 *            끝 연도 정보(반영을 하지 않을 경우에는 끝연도는 -1이다.)
	 * 
	 * @param excelFilePath
	 *            Excel 파일이 존재하는 경로
	 * 
	 * @param executeType
	 *            실행 속성(Tracker를 실행하기 위한 속성).
	 * 
	 * @param logConsole
	 *            LogConsole를 찍기 위한 콘솔 뷰어
	 * @param citationTrackerName
	 * 
	 */
	public ParameterInfo(CitationDirection directionType, CitationTrackerOption trackerType, int startYear,
			int endYear, String excelFilePath, ExecuteType executeType, TextViewer logConsole,
			String citationTrackerName) {
		this.directionType = directionType;
		this.option = trackerType;
		this.startYear = startYear;
		this.endYear = endYear;
		this.excelFilePath = excelFilePath;
		this.executeType = executeType;
		this.logConsole = logConsole;
		this.citationTrackerName = citationTrackerName;
	}

	public String getCitationTrackerName() {
		return citationTrackerName;
	}

	public CitationDirection getDirectionType() {
		return directionType;
	}

	public CitationTrackerOption getTrackerType() {
		return option;
	}

	public int getStartYear() {
		return startYear;
	}

	public int getEndYear() {
		return endYear;
	}

	public String getInputFilePath() {
		return excelFilePath;
	}

	public ExecuteType getExeCutetype() {
		return executeType;
	}

	public TextViewer getLogConsole() {
		return logConsole;
	}

	private static final String LINE_DELIMITER = System.getProperty("line.separator");

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Citation Direction : " + directionType);
		sb.append(LINE_DELIMITER);
		sb.append("Option TrackerType : " + option);
		sb.append(LINE_DELIMITER);
		sb.append("Strat Year : " + startYear);
		sb.append(LINE_DELIMITER);
		sb.append("End Year : " + endYear);
		sb.append(LINE_DELIMITER);
		sb.append("Excel File Path : " + excelFilePath);
		sb.append(LINE_DELIMITER);
		sb.append("execute Type : " + executeType);
		sb.append(LINE_DELIMITER);
		sb.append("Citation Direction : " + directionType);
		return sb.toString();
	}
}
