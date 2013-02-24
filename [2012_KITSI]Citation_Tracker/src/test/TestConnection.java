/**
 * 
 */
package test;

import java.sql.Connection;

import kr.co.topquadrant.citationTracker.db.ConnectionFactory;

import org.junit.Test;

/**
 * @author coreawin
 * @sinse 2012. 11. 28. 
 * @version 1.0
 * @history 2012. 11. 28. : 최초 작성 <br>
 *
 */
public class TestConnection {

	@Test
	public void testGetConnection(){
		ConnectionFactory cf = ConnectionFactory.getInstance();
		Connection co = cf.getConnection();
		System.out.println(co);
		cf.release(co);
	}
}
