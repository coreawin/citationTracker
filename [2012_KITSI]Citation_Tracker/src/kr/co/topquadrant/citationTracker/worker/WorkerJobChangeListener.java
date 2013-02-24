/**
 * 
 */
package kr.co.topquadrant.citationTracker.worker;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

/**
 * @author coreawin
 * @sinse 2012. 8. 8.
 * @version 1.0
 * 
 *          <pre>
 * 2012. 8. 8. : 최초 작성
 * </pre>
 */
public class WorkerJobChangeListener extends JobChangeAdapter {
	/*
	 * 작업이 종료되었을때 실행된다.
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.JobChangeAdapter#done(org.eclipse.core.
	 * runtime.jobs.IJobChangeEvent)
	 */
	public void done(IJobChangeEvent event) {
		WorkerDispatcher.getInstance().notifyInstance();
		Worker worker = (Worker) event.getJob();
		worker.finished();
	}
}
