/**
 * 
 */
package kr.co.topquadrant.citationTracker.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author coreawin
 * @sinse 2012. 12. 5.
 * @version 1.0
 * @history 2012. 12. 5. : 최초 작성 <br>
 * 
 */
public class ScopusDataInfo {

	public static final Map<String, String> CITATION_TYPE_INFO_MAP = new HashMap<String, String>();

	static {
		ConnectionFactory cf = ConnectionFactory.getInstance();
		Connection conn = null;
		try {
			conn = cf.getConnection();
			initCitationType(conn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	private static void initCitationType(Connection conn) throws SQLException {
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			String query = SQLQuery.CITATION_TYPE;
			psmt = conn.prepareStatement(query);
			rs = psmt.executeQuery();
			while (rs.next()) {
				String type = rs.getString(1);
				String desc = rs.getString(2);
				CITATION_TYPE_INFO_MAP.put(type.toUpperCase().trim(), desc.trim());
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (psmt != null)
				psmt.close();
		}
	}
}
