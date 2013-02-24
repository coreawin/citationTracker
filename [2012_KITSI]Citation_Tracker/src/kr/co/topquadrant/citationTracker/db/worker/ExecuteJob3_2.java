/**
 * 
 */
package kr.co.topquadrant.citationTracker.db.worker;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import kr.co.topquadrant.citationTracker.ResultWriter;
import kr.co.topquadrant.citationTracker.co.BuildData;
import kr.co.topquadrant.citationTracker.console.ConsoleOut;
import kr.co.topquadrant.citationTracker.data.ParameterInfo;
import kr.co.topquadrant.citationTracker.db.DBUtil;
import kr.co.topquadrant.citationTracker.db.SQLQuery;
import kr.co.topquadrant.citationTracker.resource.bundle.SystemResourceBundle;

/**
 * @author coreawin
 * @sinse 2012. 11. 30.
 * @version 1.0
 * @history 2012. 11. 30. : 최초 작성 <br>
 * 
 */
public class ExecuteJob3_2 extends ExecuteJob {

	final String menuName = SystemResourceBundle.getInstance().getResourceBundle().getMessage("execute_Type4");

	private final String repositoryPath = "./work";
	private BuildData buildData = new BuildData(repositoryPath);

	/**
	 * @param p
	 * @throws Exception
	 */
	public ExecuteJob3_2(ParameterInfo p) throws Exception {
		super(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#exeForward()
	 */
	@Override
	protected void exeForward() throws Exception {
		ConsoleOut.getInsance().error("Forward 옵션은 [" + menuName + "] 항목에서는 지원되지 않습니다.");
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#exeBackward()
	 */
	@Override
	protected void exeBackward() throws Exception {
//		if (parameter.getTrackerType() == CitationTrackerOption.Inside) {
//			ConsoleOut.getInsance().error("Inside 옵션은 [" + menuName + "] 항목에서는 지원되지 않습니다");
//			return;
//		}
		executeJob(SQLQuery.Worker3.REFERENCE_OF_REFERENCE);
	}

	private void executeJob(String sqlQuery) throws Exception {
		if (inputEidSets.size() == 0) {
			setupAllEIDSet();
		}
		conn = cf.getConnection();
		try {
			int retrieveCnt = 1;
			ConsoleOut.getInsance().info("데이터를 조회 및 구축합니다.");
			for (List<String> list : datas) {
				if (retrieveCnt % 100 == 0) {
					ConsoleOut.getInsance().info("데이터를 조회 및 구축하고 있습니다." + (retrieveCnt) + "/" + inputEidSets.size());
				}
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
					String refeid = rs.getString(2);
					String refrefid = rs.getString(3);
					
					if (inside) {
						if (inputEidSets.contains(refeid)) {
							buildData.buildOccurrenceData(eid, refeid);
						}
						if (inputEidSets.contains(refrefid)) {
							buildData.buildOccurrenceData(eid, refrefid);
						}
					} else {
						buildData.buildOccurrenceData(eid, refeid);
						buildData.buildOccurrenceData(eid, refrefid);
					}
				}
				cf.release(rs, psmt);
				retrieveCnt++;
			}
			buildData.flush();
			ConsoleOut.getInsance().info("데이터를 조회 및 구축이 완료되었습니다." + retrieveCnt);
		} catch (Exception e) {
			throw e;
		} finally {
			cf.release(rs, psmt, conn);
			makeOccurrence();
		}
	}

	private void makeOccurrence() throws Exception {
		Map<String, Integer> occurrence = buildData.getOccurrenceData();
		List<Map<String, Integer>> resultList = convertData(occurrence);
		writeResult(resultList, occurrence);
	}

	private void writeResult(List<Map<String, Integer>> list, Map<String, Integer> occurrence) throws Exception {
		writer.println("eid(source)" + ResultWriter.COMMA + "eid(source)" + ResultWriter.COMMA + "co-occurrence"
				+ ResultWriter.COMMA + "eid(source) total citation" + ResultWriter.COMMA + "eid(source) total citation");
		conn = cf.getConnection();
		try {
			int retrieveCnt = 1;
			Map<String, Integer> tc = new TreeMap<String, Integer>();
			for (Map<String, Integer> map : list) {
				Set<String> eidSet = new TreeSet<String>();
				for (String keys : map.keySet()) {
					String[] keyArray = keys.split(BuildData.KEY_DELIMITER);
					eidSet.add(keyArray[0]);
					eidSet.add(keyArray[1]);
				}
				ConsoleOut.getInsance().info("데이터를 조회 및 구축하고 있습니다." + (retrieveCnt++) * 100 + "/" + datas.size() * 100);
				String query = String
						.format(SQLQuery.Worker2.TOTAL_CITATION, DBUtil.preparePlaceHolders(eidSet.size()));
				psmt = conn.prepareStatement(query);
				int idx = 1;
				for (String eid : eidSet) {
					psmt.setString(idx++, eid);
				}
				rs = psmt.executeQuery();
				while (rs.next()) {
					String eid = rs.getString(1);
					int count = rs.getInt(2);
					tc.put(eid, count);
				}
				cf.release(rs, psmt);
			}

			for (String keys : occurrence.keySet()) {
				String[] keyArray = keys.split(BuildData.KEY_DELIMITER);
				String e1 = keyArray[0];
				String e2 = keyArray[1];
				int t1 = tc.get(e1) == null ? 0 : tc.get(e1);
				int t2 = tc.get(e2) == null ? 0 : tc.get(e2);
				writer.println(e1 + ResultWriter.COMMA + e2 + ResultWriter.COMMA + occurrence.get(keys)
						+ ResultWriter.COMMA + t1 + ResultWriter.COMMA + t2);
			}

			ConsoleOut.getInsance().info("데이터를 조회 및 구축이 완료되었습니다.");
		} catch (Exception e) {
			throw e;
		} finally {
			cf.release(rs, psmt, conn);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#closeResource()
	 */
	@Override
	public void closeResource() {
		if (buildData != null) {
			buildData.close();
		}
	}

}
