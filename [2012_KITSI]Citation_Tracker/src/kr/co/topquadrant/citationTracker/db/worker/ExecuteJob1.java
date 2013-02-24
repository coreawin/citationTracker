/**
 * 
 */
package kr.co.topquadrant.citationTracker.db.worker;

import java.io.IOException;
import java.util.List;

import kr.co.topquadrant.citationTracker.ResultWriter;
import kr.co.topquadrant.citationTracker.console.ConsoleOut;
import kr.co.topquadrant.citationTracker.data.ParameterInfo;
import kr.co.topquadrant.citationTracker.db.DBUtil;
import kr.co.topquadrant.citationTracker.db.SQLQuery;

/**
 * @author coreawin
 * @sinse 2012. 11. 30.
 * @version 1.0
 * @history 2012. 11. 30. : 최초 작성 <br>
 * 
 */
public class ExecuteJob1 extends ExecuteJob {

	/**
	 * @param excelPath
	 * @throws IOException
	 */
	public ExecuteJob1(ParameterInfo p) throws Exception {
		super(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#exeForward()
	 */
	@Override
	protected void exeForward() throws Exception {
		executeJob(SQLQuery.Worker1.FORWARD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#exeBackward()
	 */
	@Override
	protected void exeBackward() throws Exception {
		executeJob(SQLQuery.Worker1.BACKWARD);
	}

	private void executeJob(String sqlQuery) throws Exception {
		conn = cf.getConnection();
		try {
			int retrieveCnt = 1;
			writer.println("eid(source)" + ResultWriter.COMMA + "year" + ResultWriter.COMMA + "citation "
					+ parameter.getDirectionType().toString() + "eid" + ResultWriter.COMMA + "year (citation 발생연도)");
			for (List<String> list : datas) {
				ConsoleOut.getInsance().info("데이터를 조회하고 있습니다." + (retrieveCnt++) * 100 + "/" + datas.size() * 100);
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
					if (cpby == null)
						cpby = DUMMY;
					if ("null".equalsIgnoreCase(cpby.trim()))
						cpby = DUMMY;
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
		} catch (Exception e) {
			throw e;
		} finally {
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
