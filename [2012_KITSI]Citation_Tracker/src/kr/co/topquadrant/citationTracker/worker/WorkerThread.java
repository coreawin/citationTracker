package kr.co.topquadrant.citationTracker.worker;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author coreawin
 * @sinse 2012. 8. 8.
 * @version 1.0
 * 
 *          <pre>
 * 2012. 8. 8. : 최초 작성
 * </pre>
 */
public class WorkerThread extends Thread {

	/**
	 * Executor 현재 상태
	 * 
	 * @author coreawin
	 * @sinse 2012. 8. 8.
	 * @version 1.0
	 * 
	 *          <pre>
	 * 2012. 8. 8. : 최초 작성
	 * </pre>
	 */
	public enum ExecutorStatus {
		RUNNING, EXCEPTION, COMPLETE, CANCEL;
	};

	/**
	 * 종료 플래그 <br>
	 * 
	 * @see ExecutorStatus
	 */
	private BlockingQueue<ExecutorStatus> finishedFlag = new ArrayBlockingQueue<ExecutorStatus>(1);

	private Worker worker;
	
	private WorkerThread executor = null;

	public WorkerThread(Worker analysisWorker) {
		this.worker = analysisWorker;
	}

	public String getWorkerName() {
		return worker.getName();
	}

	public void run() {
		try {
			worker.execute();
			finishedFlag.add(ExecutorStatus.COMPLETE);
		} catch (Exception e) {
			e.printStackTrace();
			finishedFlag.add(ExecutorStatus.EXCEPTION);
		} finally {
			if (isInterrupted()) {
				finishedFlag.clear();
				finishedFlag.add(ExecutorStatus.EXCEPTION);
			}
			synchronized (worker) {
				try {
					worker.notify();
				} finally {
				}
			}
			worker.closeResource();
		}
	}

	/**
	 * Retrieves and removes the head of this queue, or returns null if this
	 * queue is empty.
	 * 
	 * Returns: the head of this queue, or null if this queue is empty
	 * 
	 * @return true:정상종료<br>
	 *         false:비정상종료 <br>
	 * @throws InterruptedException
	 */
	public ExecutorStatus getFinished() throws InterruptedException {
		return finishedFlag.poll();
	}

	public void notifyFinishedFlag() {
		finishedFlag.notify();
	}

}
