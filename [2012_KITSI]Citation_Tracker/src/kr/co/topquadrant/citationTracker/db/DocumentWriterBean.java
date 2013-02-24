/**
 * 
 */
package kr.co.topquadrant.citationTracker.db;

import java.util.HashMap;
import java.util.Map;

/**
 * @author coreawin
 * @sinse 2012. 12. 5.
 * @version 1.0
 * @history 2012. 12. 5. : 최초 작성 <br>
 * 
 */
public class DocumentWriterBean {
	private String eid;
	private String sourceid;
	private String sourceTitle;
	private String publicationYear;
	private String citationType;
	private float avgPby;
	private Map<Integer, Integer> cPbyStat = new HashMap<Integer, Integer>();

	public void setPbyStat(int pby, int cnt) {
		this.cPbyStat.put(pby, cnt);
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getSourceid() {
		return sourceid;
	}

	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}

	public String getSourceTitle() {
		return sourceTitle;
	}

	public void setSourceTitle(String sourceTitle) {
		this.sourceTitle = sourceTitle;
	}

	public String getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getCitationType() {
		return ScopusDataInfo.CITATION_TYPE_INFO_MAP.get(citationType.toUpperCase().trim());
	}

	public void setCitationType(String citationType) {
		this.citationType = citationType;
	}

	public Map<Integer, Integer> getcPbyStat() {
		return cPbyStat;
	}

	public float getAvgPby() {
		return avgPby;
	}

	public void setAvgPby(float avgPby) {
		this.avgPby = avgPby;
	}
}
