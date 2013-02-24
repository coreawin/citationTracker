package kr.co.topquadrant.citationTracker.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.topquadrant.common.db.connection.AConnectionPoolFactory;
import kr.co.topquadrant.common.db.connection.prop.AConnectionPoolProperties;
import kr.co.topquadrant.common.db.connection.prop.ConnectionPropertiesCommon;

/**
 * DB 접속 정보는 프로퍼티 파일에서 로드한다.<br>
 * 
 * @author neon
 * 
 */
public class ConnectionFactory extends AConnectionPoolFactory {

	private static ConnectionFactory instance;

	/**
	 * 싱글톤을 위한 인스턴스 반환 메소드<br>
	 * 
	 * @return
	 */
	public static synchronized ConnectionFactory getInstance() {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		return instance;
	}

	public void close(Connection conn) throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}

	@Override
	public void release(ResultSet rs, PreparedStatement psmt, Connection conn) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (psmt != null)
			try {
				psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

	}

	@Override
	public void release(ResultSet rs, PreparedStatement psmt) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (psmt != null)
			try {
				psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void release(PreparedStatement psmt, Connection conn) {
		if (psmt != null)
			try {
				psmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	@Override
	protected AConnectionPoolProperties createConnectionProperty() {
		return new ConnectionPropertiesCommon(ConnectionInfo.USER, ConnectionInfo.PWD,
				ConnectionInfo.getConnectionURL(), ConnectionInfo.DRIVER_NAME);
	}
}
