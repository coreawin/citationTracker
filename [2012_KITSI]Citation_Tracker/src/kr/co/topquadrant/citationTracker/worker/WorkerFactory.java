/**
 * 
 */
package kr.co.topquadrant.citationTracker.worker;

import kr.co.topquadrant.citationTracker.ui.ClusterTrackerMainUI.ExecuteType;

/**
 * @author coreawin
 * @sinse 2012. 8. 8.
 * @version 1.0
 * 
 *          <pre>
 * 2012. 8. 8. : 최초 작성
 * </pre>
 */
public class WorkerFactory {

	/**
	 * 클러스터 실행 인스턴스를 얻는다.<br>
	 * 
	 * @param name
	 *            실행 쓰레드 이름.
	 * @return
	 */
	public static Worker createWorker(ExecuteType type, String name) {
		if (name == null) {
			throw new RuntimeException(Worker.Message.ERROR_INSTANCE_NULL);
		} else {
			name = name.trim();
		}
		Worker instance = new ExecutorWorker(name);
		instance.addJobChangeListener(new WorkerJobChangeListener());
		return instance;
	}
}
