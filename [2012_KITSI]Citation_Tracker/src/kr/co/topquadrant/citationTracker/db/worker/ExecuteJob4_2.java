/**
 * 
 */
package kr.co.topquadrant.citationTracker.db.worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class ExecuteJob4_2 extends ExecuteJob4_1 {

	/**
	 * @param excelPath
	 * @throws IOException
	 */
	public ExecuteJob4_2(ParameterInfo p) throws Exception {
		super(p);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#exeForward()
	 */
	@Override
	protected void exeForward() throws Exception {
		getJournalEid();
		executeJobForward(SQLQuery.Worker4.FORWARD_YEAR_STAT);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see kr.co.topquadrant.citationTracker.db.worker.ExecuteJob#exeBackward()
	 */
	@Override
	protected void exeBackward() throws Exception {
		getJournalEid();
		executeJobBackward(SQLQuery.Worker4.BACKWARD_YEAR_STAT);
	}

	/**
	 * @throws Exception 
	 * 
	 */
	private void getJournalEid() throws Exception {
		conn = cf.getConnection();
		try {
			super.setupAllEIDSet();
			for (List<String> list : datas) {
				ConsoleOut.getInsance().info("저널 정보를 추출하고 있습니다.");
				String query = String.format(SQLQuery.Worker4.JOURNAL_ALL, DBUtil.preparePlaceHolders(list.size()));
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
					inputEidSets.add(eid);
				}
				cf.release(rs, psmt);
			}
			setNewDatas();
		} catch (Exception e) {
			throw e;
		} finally {
			cf.release(rs, psmt, conn);
		}
	}

	/**
	 * 
	 */
	private void setNewDatas() {
		datas.clear();
		List<String> list = new ArrayList<String>();
		for(String eid : inputEidSets){
			if(list.size()==1000){
				datas.add(list);
				list = new ArrayList<String>();
			}else{
				list.add(eid);
			}
		}
		datas.add(list);
	}

}
