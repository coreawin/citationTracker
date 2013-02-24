package kr.co.topquadrant.citationTracker.worker;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.ILock;
import org.eclipse.core.runtime.jobs.Job;

/**
 * Ŭ������ �м��� ���� �۾��� �����Ѵ�.
 * 
 * @author coreawin
 * 
 */
public class WorkerDispatcher extends Job {

	private Worker runningWorker = null;

	/**
	 * JobExecutor�� ���� ���¸� �����Ѵ�.
	 * 
	 * @author coreawin
	 * 
	 */
	public enum AnalysisDispatcherStatus {
		RUNNING, WATING;
	}

	private AnalysisDispatcherStatus status = AnalysisDispatcherStatus.WATING;

	private WorkerDispatcher(String name) {
		super(name);
		this.schedule();
	}

	private static final WorkerDispatcher instance = new WorkerDispatcher(String.valueOf(System.currentTimeMillis()));

	public static WorkerDispatcher getInstance() {
		return instance;
	}

	private static LinkedBlockingQueue<Worker> queue = new LinkedBlockingQueue<Worker>(1);

	@SuppressWarnings("unchecked")
	public synchronized boolean cancelJob() {
		queue.remove(runningWorker);
		boolean cancel = runningWorker.cancel();
		// System.out.println("cancel " + cancel);
		runningWorker.setCancel(true);
		System.out.println("AnalysisWorkerExecutor RUNNING? " + (runningWorker.getState() == Job.RUNNING));
		synchronized (runningWorker) {
			runningWorker.notifyAll();
		}
		return cancel;
	}

	public void removeJob(Worker worker) {
		queue.remove(worker);
	}

	public void addJob(Worker worker) {
		queue.add(worker);
		notifyInstance();
	}

	public ILock lock = getJobManager().newLock();

	@Override
	protected IStatus run(IProgressMonitor arg0) {
		lock.acquire();
		try {
			while (true) {
				try {
					status = AnalysisDispatcherStatus.WATING;
					// System.out.println("���ο� �۾��� ����ϰ� �ֽ��ϴ�.");
					runningWorker = queue.take();
					runningWorker.join();
					runningWorker.schedule();
					status = AnalysisDispatcherStatus.RUNNING;
					while (runningWorker.getState() == Job.RUNNING || runningWorker.getState() == Job.WAITING) {
						synchronized (this) {
							try {
								this.wait();
							} finally {
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} finally {
			lock.release();
		}
	}

	public int countJob() {
		return queue.size();
	}

	/**
	 * ���� �۾�ť�� ���¸� �˸���.
	 * 
	 * @return
	 */
	public AnalysisDispatcherStatus getStatus() {
		return status;
	}

	public void notifyInstance() {
		synchronized (this) {
			try {
				this.notifyAll();
			} finally {
			}
		}
	}

}
