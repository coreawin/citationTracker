package kr.co.topquadrant.citationTracker.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.topquadrant.common.db.connection.AConnectionPoolFactory;
import kr.co.topquadrant.common.db.connection.prop.AConnectionPoolProperties;
import kr.co.topquadrant.common.db.connection.prop.ConnectionPropertiesCommon;

/**
 * DB ���� ������ ������Ƽ ���Ͽ��� �ε��Ѵ�.<br>
 * 
 * @author neon
 * 
 */
public class ConnectionFactory extends AConnectionPoolFactory {

	private static ConnectionFactory instance;

	/**
	 * �̱����� ���� �ν��Ͻ� ��ȯ �޼ҵ�<br>
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
