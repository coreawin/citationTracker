package kr.co.topquadrant.citationTracker.data;

import kr.co.topquadrant.citationTracker.ui.ClusterTrackerMainUI.CitationDirection;
import kr.co.topquadrant.citationTracker.ui.ClusterTrackerMainUI.CitationTrackerOption;
import kr.co.topquadrant.citationTracker.ui.ClusterTrackerMainUI.ExecuteType;

import org.eclipse.jface.text.TextViewer;

/**
 * CitationTracker �����ϱ� ���� �����ϴ� �Ķ���� �����̴�.
 * 
 * @author �̰���
 * @sinse 2012. 11. 27.
 * @version 1.0
 * @history 2012. 11. 27. : �����ۼ�
 */
public class ParameterInfo {

	/**
	 * Citation Direction ����
	 * 
	 * Forward �Ǵ� Backward
	 * 
	 * directionType
	 */
	private CitationDirection directionType;
	/**
	 * CitationTracker Option ����
	 * 
	 * Inside or Outside
	 * 
	 * trackerType
	 */
	private CitationTrackerOption option;

	/**
	 * ���� ����(�ݿ��� ���� ���� ��쿡�� ���� ������ �������� -1�̴�.)
	 */
	private int startYear;
	private int endYear;

	/**
	 * Excel File�� �ִ� ����̴�, excelFilePath
	 */
	private String excelFilePath;

	/**
	 * 
	 * ���� ���� Ÿ���� �����̴�.
	 * 
	 * exeCutetype
	 */
	private ExecuteType executeType;

	/**
	 * LogConsole�� ��� ������Ʈ �̴�. logConsole
	 */
	private TextViewer logConsole;
	private String citationTrackerName;

	/**
	 * 
	 * 
	 * @param directionType
	 *            Citation Direction ����
	 * 
	 * @param trackerType
	 *            CitationTracker Option ����
	 * 
	 * @param startYear
	 *            ���� ���� ����(�ݿ��� ���� ���� ��쿡�� ���� ������ -1�̴�.)
	 * 
	 * @param endYear
	 *            �� ���� ����(�ݿ��� ���� ���� ��쿡�� �������� -1�̴�.)
	 * 
	 * @param excelFilePath
	 *            Excel ������ �����ϴ� ���
	 * 
	 * @param executeType
	 *            ���� �Ӽ�(Tracker�� �����ϱ� ���� �Ӽ�).
	 * 
	 * @param logConsole
	 *            LogConsole�� ��� ���� �ܼ� ���
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
