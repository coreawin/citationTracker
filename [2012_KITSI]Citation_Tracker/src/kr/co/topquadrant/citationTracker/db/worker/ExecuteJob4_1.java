/**
 * 
 */
package kr.co.topquadrant.citationTracker.db.worker;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import kr.co.topquadrant.citationTracker.ResultWriter;
import kr.co.topquadrant.citationTracker.console.ConsoleOut;
import kr.co.topquadrant.citationTracker.data.ParameterInfo;
import kr.co.topquadrant.citationTracker.db.DBUtil;
import kr.co.topquadrant.citationTracker.db.DocumentWriterBean;
import kr.co.topquadrant.citationTracker.db.SQLQuery;

/**
 * @author coreawin
 * @sinse 2012. 11. 30.
 * @version 1.0
 * @history 2012. 11. 30. : 최초 작성 <br>
 * 
 */
public class ExecuteJob4_1 extends ExecuteJob {

	/**
	 * @param excelPath
	 * @throws IOException
	 */
	public ExecuteJob4_1(ParameterInfo p) throws Exception {
		super(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#exeForward()
	 */
	@Override
	protected void exeForward() throws Exception {
		executeJobForward(SQLQuery.Worker4.FORWARD_YEAR_STAT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#exeBackward()
	 */
	@Override
	protected void exeBackward() throws Exception {
		executeJobBackward(SQLQuery.Worker4.BACKWARD_YEAR_STAT);
	}

	protected void executeJobForward(String sqlQuery) throws Exception {
		conn = cf.getConnection();
		Map<String, DocumentWriterBean> info = new LinkedHashMap<String, DocumentWriterBean>();
		int maxYear = Integer.MIN_VALUE;
		int minYear = Integer.MAX_VALUE;
		try {
			int retrieveCnt = 1;
			for (List<String> list : datas) {
				ConsoleOut.getInsance().info("데이터를 조회하고 있습니다." + retrieveCnt);
				String query = String.format(sqlQuery, DBUtil.preparePlaceHolders(list.size()));
				psmt = conn.prepareStatement(query);
				int idx = 1;
				for (String eid : list) {
					psmt.setString(idx++, eid);
					retrieveCnt++;
				}
				psmt.setInt(idx++, parameter.getStartYear());
				psmt.setInt(idx++, parameter.getEndYear());
				rs = psmt.executeQuery();
				while (rs.next()) {
					String eid = rs.getString(1);
					int cnt = Integer.parseInt(rs.getString(2));
					int pby = Integer.parseInt(rs.getString(3));
					maxYear = Math.max(maxYear, pby);
					minYear = Math.min(minYear, pby);
					DocumentWriterBean d = null;
					if (info.containsKey(eid)) {
						d = info.get(eid);
					} else {
						d = new DocumentWriterBean();
						d.setEid(eid);
					}
					d.setPbyStat(pby, cnt);
					info.put(eid, d);
				}
				cf.release(rs, psmt);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cf.release(rs, psmt, conn);
		}
		info = getDocumentInfo(info);
		printFoward(info, maxYear, minYear);
	}

	protected void executeJobBackward(String sqlQuery) throws Exception {
		conn = cf.getConnection();
		Map<String, DocumentWriterBean> info = new LinkedHashMap<String, DocumentWriterBean>();
		try {
			int retrieveCnt = 1;
			ConsoleOut.getInsance().info("데이터의 상세 정보를 조회 및 구축하고 있습니다.");
			for (List<String> list : datas) {
				String query = String.format(sqlQuery, DBUtil.preparePlaceHolders(list.size()));
				psmt = conn.prepareStatement(query);
				int idx = 1;
				for (String eid : list) {
					psmt.setString(idx++, eid);
				}
				psmt.setInt(idx++, parameter.getStartYear());
				psmt.setInt(idx++, parameter.getEndYear());
				rs = psmt.executeQuery();
				while (rs.next()) {
					String eid = rs.getString(1);
					String publicationYear = rs.getString(2);
					float avgPby = rs.getFloat(3);
					DocumentWriterBean d = new DocumentWriterBean();
					d.setEid(eid);
					d.setPublicationYear(publicationYear);
					d.setAvgPby(avgPby);
					info.put(eid, d);
				}
				cf.release(rs, psmt);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cf.release(rs, psmt, conn);
		}
		info = getDocumentInfo(info);
		printBackWard(info);
	}

	/**
	 * @param info
	 * @throws Exception
	 */
	private void printBackWard(Map<String, DocumentWriterBean> info) throws Exception {
		String title = "eid (source)" + ResultWriter.COMMA + "eid (source) type" + ResultWriter.COMMA + "eid source"
				+ ResultWriter.COMMA + "eid (source) pub year" + ResultWriter.COMMA + "backward refs avg pby"
				+ ResultWriter.COMMA + "차이";
		try {
			ConsoleOut.getInsance().info("결과 데이터를 기록하고 있습니다.");
			writer.println(title);
			for (DocumentWriterBean d : info.values()) {
				writer.print(d.getEid());
				writer.print(String.valueOf(ResultWriter.COMMA));
				writer.print(d.getCitationType());
				writer.print(String.valueOf(ResultWriter.COMMA));
				writer.print(d.getSourceTitle());
				writer.print(String.valueOf(ResultWriter.COMMA));
				writer.print(d.getPublicationYear());
				writer.print(String.valueOf(ResultWriter.COMMA));
				writer.print(String.valueOf(d.getAvgPby()));
				writer.print(String.valueOf(ResultWriter.COMMA));
				writer.println("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {

		}
	}

	protected Map<String, DocumentWriterBean> getDocumentInfo(Map<String, DocumentWriterBean> info) throws Exception {
		conn = cf.getConnection();
		try {
			ConsoleOut.getInsance().info("데이터의 상세 정보를 조회 및 구축하고 있습니다.");
			for (List<String> list : datas) {
				String query = String.format(SQLQuery.Worker4.DOCUMENT_INFO, DBUtil.preparePlaceHolders(list.size()));
				psmt = conn.prepareStatement(query);
				int idx = 1;
				for (String eid : list) {
					psmt.setString(idx++, eid);
				}
				rs = psmt.executeQuery();
				while (rs.next()) {
					String eid = rs.getString(1);
					String pby = rs.getString(2);
					String sourceTitle = rs.getString(3);
					String citationType = rs.getString(4);
					DocumentWriterBean d = null;
					if (info.containsKey(eid)) {
						d = info.get(eid);
					} else {
						d = new DocumentWriterBean();
						d.setEid(eid);
					}
					d.setSourceTitle(sourceTitle);
					d.setPublicationYear(pby);
					d.setCitationType(citationType);
					info.put(eid, d);
				}
				cf.release(rs, psmt);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cf.release(rs, psmt, conn);
		}
		return info;
	}

	protected void printFoward(Map<String, DocumentWriterBean> info, int maxYear, int minYear) throws Exception {
		String title = "eid (source)" + ResultWriter.COMMA + "eid (source) type" + ResultWriter.COMMA + "eid source"
				+ ResultWriter.COMMA + "eid (source) pub year" + ResultWriter.COMMA;
		Set<Integer> yearInfo = new TreeSet<Integer>();
		for (int i = minYear; i <= maxYear; i++) {
			yearInfo.add(i);
			title += i;
			if (i != maxYear) {
				title += ResultWriter.COMMA;
			}
		}
		try {
			ConsoleOut.getInsance().info("결과 데이터를 기록하고 있습니다.");
			writer.println(title);
			for (DocumentWriterBean d : info.values()) {
				writer.print(d.getEid());
				writer.print(String.valueOf(ResultWriter.COMMA));
				writer.print(d.getCitationType());
				writer.print(String.valueOf(ResultWriter.COMMA));
				writer.print(d.getSourceTitle());
				writer.print(String.valueOf(ResultWriter.COMMA));
				writer.print(d.getPublicationYear());
				writer.print(String.valueOf(ResultWriter.COMMA));
				Map<Integer, Integer> cPbyStat = d.getcPbyStat();
				for (int year : yearInfo) {
					int cnt = 0;
					if (cPbyStat.containsKey(year)) {
						cnt = cPbyStat.get(year);
					}
					writer.print(String.valueOf(cnt));
					if (year != maxYear) {
						writer.print(String.valueOf(ResultWriter.COMMA));
					}
				}
				writer.println("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#close()
	 */
	@Override
	public void closeResource() {

	}

}
