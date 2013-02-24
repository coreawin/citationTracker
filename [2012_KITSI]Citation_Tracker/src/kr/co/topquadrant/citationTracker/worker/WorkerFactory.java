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
 * 2012. 8. 8. : ���� �ۼ�
 * </pre>
 */
public class WorkerFactory {

	/**
	 * Ŭ������ ���� �ν��Ͻ��� ��´�.<br>
	 * 
	 * @param name
	 *            ���� ������ �̸�.
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
