/**
 * 
 */
package kr.co.topquadrant.citationTracker.db.worker;

import java.io.IOException;

import kr.co.topquadrant.citationTracker.ResultWriter;
import kr.co.topquadrant.citationTracker.console.ConsoleOut;
import kr.co.topquadrant.citationTracker.data.ParameterInfo;
import kr.co.topquadrant.citationTracker.db.SQLQuery;

/**
 * @author coreawin
 * @sinse 2012. 11. 30.
 * @version 1.0
 * @history 2012. 11. 30. : 최초 작성 <br>
 * 
 */
public class ExecuteJob5 extends ExecuteJob {

	/**
	 * @param excelPath
	 * @throws IOException
	 */
	public ExecuteJob5(ParameterInfo p) throws Exception {
		super(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#exeForward()
	 */
	@Override
	protected void exeForward() throws Exception {
		executeJob();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#exeBackward()
	 */
	@Override
	protected void exeBackward() throws Exception {
		executeJob();
	}

	private void executeJob() throws Exception {
		setupAllEIDSet();
		conn = cf.getConnection();
		try {
			writer.println("eid (source)" + ResultWriter.COMMA + "eid (source)" + ResultWriter.COMMA + "path=1"+ ResultWriter.COMMA + "path=2");
			int skipSource1Cnt = 1;
			ConsoleOut.getInsance().info("데이터를 조회하고 있습니다. [총건수: " + inputEidSets.size() + "]");
			for (String source1 : inputEidSets) {
				if (skipSource1Cnt % 1000 == 0) {
					ConsoleOut.getInsance().info("데이터를 조회하고 있습니다." + (skipSource1Cnt) + "/" + inputEidSets.size());
				}
				int skipSource2Cnt = 0;
				for (String source2 : inputEidSets) {
					skipSource2Cnt++;
					if (skipSource1Cnt >= skipSource2Cnt) {
						continue;
					}
					psmt = conn.prepareStatement(SQLQuery.Worker5.PATH1);
					psmt.clearParameters();
					int idx = 1;
					psmt.setString(idx++, source1);
					psmt.setString(idx++, source2);
					// psmt.setInt(idx++, parameter.getStartYear());
					// psmt.setInt(idx++, parameter.getEndYear());
					rs = psmt.executeQuery();
					String path1 = "0";
					String path2 = "0";
					while (rs.next()) {
						path1 = String.valueOf(rs.getInt(3));
					}
					
					psmt.close();
					rs.close();

					psmt = conn.prepareStatement(SQLQuery.Worker5.PATH2);
					idx = 1;
					psmt.setString(idx++, source1);
					psmt.setString(idx++, source2);
					if("0".equals(path1) && "0".equals(path2)){
						continue;
					}
					if (inside) {
						if (inputEidSets.contains(path1)) {
							writer.println(source1 + ResultWriter.COMMA + source2 + ResultWriter.COMMA + path1
									+ ResultWriter.COMMA + path2);
						}
					} else {
						writer.println(source1 + ResultWriter.COMMA + source2 + ResultWriter.COMMA + path1
								+ ResultWriter.COMMA + path2);
					}
					psmt.close();
					rs.close();
				}
				writer.flush();
				skipSource1Cnt++;
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
