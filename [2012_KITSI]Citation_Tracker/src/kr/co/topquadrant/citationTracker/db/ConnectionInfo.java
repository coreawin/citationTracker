/**
 * 
 */
package kr.co.topquadrant.citationTracker.db;

/**
 * @author coreawin
 */
public class ConnectionInfo {

	public static final String DRIVER_ORACLE = ConnectionFactory.getOracleDBDriverName();
	public static final String DRIVER_MYSQL = ConnectionFactory.getMySqlDBDriverName();
	public static final String DRIVER_DERBY = "org.apache.derby.jdbc.ClientDriver";

	public static String IP = "203.250.196.44";
	public static int PORT = 1551;
	public static String DBNAME = "KISTI5";
	public static String USER = "scopus";
	public static String PWD = "scopus+11";
	public static String DRIVER_NAME = DRIVER_ORACLE;
	public static String PROP = "";

	public static String getConnectionURL() {
		if (DRIVER_NAME.equals(DRIVER_MYSQL)) {
			return "jdbc:mysql://" + IP + ":" + PORT + "/" + DBNAME + PROP;
		} else if (DRIVER_NAME.equals(DRIVER_DERBY)) {
			return "jdbc:derby://" + IP + ":" + PORT + "/" + DBNAME + PROP;
		} else if (DRIVER_NAME.equals(DRIVER_ORACLE)) {
			return "jdbc:oracle:thin:@" + IP + ":" + PORT + ":" + DBNAME + PROP;
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("USER :" + USER);
		sb.append("\n");
		sb.append("PWD :" + PWD);
		sb.append("\n");
		sb.append("DRIVER_NAME :" + DRIVER_NAME);
		sb.append("\n");
		sb.append("Connection URL :" + getConnectionURL());
		return sb.toString();
	}

}
