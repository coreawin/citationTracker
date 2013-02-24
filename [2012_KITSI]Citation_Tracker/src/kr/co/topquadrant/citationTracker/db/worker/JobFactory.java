/**
 * 
 */
package kr.co.topquadrant.citationTracker.db.worker;

import java.io.IOException;

import kr.co.topquadrant.citationTracker.data.ParameterInfo;

/**
 * @author coreawin
 * @sinse 2012. 11. 30.
 * @version 1.0
 * @history 2012. 11. 30. : 최초 작성 <br>
 * 
 */
public class JobFactory {

	/**
	 * 클러스터 실행 인스턴스를 얻는다.<br>
	 * 
	 * @param name
	 *            실행 쓰레드 이름.
	 * @return
	 * @throws IOException
	 */
	public static ExecuteJob createExecuteJob(ParameterInfo p) throws Exception {
		switch (p.getExeCutetype()) {
		case ExeType1:
			return new ExecuteJob1(p);
		case ExeType2:
			return new ExecuteJob2(p);
		case ExeType3:
			return new ExecuteJob3_1(p);
		case ExeType4:
			return new ExecuteJob3_2(p);
		case ExeType5:
			return new ExecuteJob4_1(p);
		case ExeType6:
			return new ExecuteJob4_2(p);
		case ExeType7:
			return new ExecuteJob5(p);
		default:
			break;
		}
		return null;
	}
}
