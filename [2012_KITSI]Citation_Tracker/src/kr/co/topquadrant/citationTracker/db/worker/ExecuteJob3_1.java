/**
 * 
 */
package kr.co.topquadrant.citationTracker.db.worker;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import kr.co.topquadrant.citationTracker.ResultWriter;
import kr.co.topquadrant.citationTracker.console.ConsoleOut;
import kr.co.topquadrant.citationTracker.data.ParameterInfo;
import kr.co.topquadrant.citationTracker.db.DBUtil;
import kr.co.topquadrant.citationTracker.db.SQLQuery;
import kr.co.topquadrant.citationTracker.resource.bundle.SystemResourceBundle;
import kr.co.topquadrant.citationTracker.ui.ClusterTrackerMainUI.CitationTrackerOption;

/**
 * @author coreawin
 * @sinse 2012. 11. 30.
 * @version 1.0
 * @history 2012. 11. 30. : 최초 작성 <br>
 * 
 */
public class ExecuteJob3_1 extends ExecuteJob {
	final String menuName = SystemResourceBundle.getInstance().getResourceBundle().getMessage("execute_Type3");

	/**
	 * @param excelPath
	 * @throws IOException
	 */
	public ExecuteJob3_1(ParameterInfo p) throws Exception {
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
		if (parameter.getTrackerType() == CitationTrackerOption.Inside) {
			ConsoleOut.getInsance().error("Inside 옵션은 [" + menuName + "] 항목에서는 지원되지 않습니다");
			return;
		}
		List<Set<String>> result = setupData();
		executeJob(SQLQuery.Worker1.BACKWARD, result);
	}

	private List<Set<String>> setupData() throws Exception {
		if (inputEidSets.size() == 0) {
			setupAllEIDSet();
		}
		conn = cf.getConnection();
		try {
			for (List<String> list : datas) {
				String query = String.format(SQLQuery.Worker3.REFERENCE, DBUtil.preparePlaceHolders(list.size()));
				psmt = conn.prepareStatement(query);
				int idx = 1;
				for (String eid : list) {
					psmt.setString(idx++, eid);
				}
				rs = psmt.executeQuery();
				while (rs.next()) {
					inputEidSets.add(rs.getString(1).trim());
				}
				cf.release(rs, psmt);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			cf.release(rs, psmt, conn);
		}
		List<Set<String>> result = convertData(inputEidSets);
		return result;
	}

	private void executeJob(String sqlQuery, List<Set<String>> result) throws Exception {
		conn = cf.getConnection();

		try {
			int retrieveCnt = 1;
			writer.println("eid(source)" + ResultWriter.COMMA + "year" + ResultWriter.COMMA + "citation eid"
					+ ResultWriter.COMMA + "year (citation 발생연도)");
			for (Set<String> list : result) {
				ConsoleOut.getInsance().info("데이터를 조회하고 있습니다." + (retrieveCnt++) + "/");
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
					String pby = rs.getString(2);
					String ceid = rs.getString(3);
					String cpby = rs.getString(4);
					if (inside) {
						if (inputEidSets.contains(ceid)) {
							writer.println(eid + ResultWriter.COMMA + pby + ResultWriter.COMMA + ceid
									+ ResultWriter.COMMA + cpby);
						}
					} else {
						writer.println(eid + ResultWriter.COMMA + pby + ResultWriter.COMMA + ceid + ResultWriter.COMMA
								+ cpby);
					}
				}
				writer.flush();
				cf.release(rs, psmt);
			}
			ConsoleOut.getInsance().info("데이터 조회가 완료되었습니다. " + (retrieveCnt++));
		} catch (Exception e) {
			throw e;
		} finally {
			writer.flush();
			cf.release(rs, psmt, conn);
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
