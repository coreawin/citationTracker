/**
 * 
 */
package kr.co.topquadrant.citationTracker.db;

import org.junit.Test;

/**
 * @author coreawin
 * @sinse 2012. 11. 30.
 * @version 1.0
 * @history 2012. 11. 30. : 최초 작성 <br>
 * 
 */
public class DBUtil {
	
	@Test
	public void testPreparePlaceHolders(){
		String query = SQLQuery.Worker1.BACKWARD;
		System.out.println(String.format(query, preparePlaceHolders(5)));
	}
	
	public static String preparePlaceHolders(int length) {
		StringBuilder builder = new StringBuilder(length * 2 - 1);
		for (int i = 0; i < length; i++) {
			if (i > 0)
				builder.append(',');
			builder.append('?');
		}
		return builder.toString();
	}
}
