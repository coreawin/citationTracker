package kr.co.topquadrant.citationTracker.worker;

import kr.co.topquadrant.citationTracker.console.ConsoleOut;
import kr.co.topquadrant.citationTracker.data.ParameterInfo;
import kr.co.topquadrant.citationTracker.db.worker.ExecuteJob;
import kr.co.topquadrant.citationTracker.db.worker.JobFactory;
import kr.co.topquadrant.citationTracker.job.ExecuteCitationTraceker;
import kr.co.topquadrant.citationTracker.worker.WorkerThread.ExecutorStatus;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class ExecutorWorker extends Worker {

	ExecuteJob job = null;
	public static final String CONSOLE_LOG = String.valueOf(ConsoleOut.class);
	public static final String EXECUTE_PARAMETER = String.valueOf(ParameterInfo.class);
	public static final String PARENT_SWT = String.valueOf(ExecuteCitationTraceker.class);
	public static final String RESULT_FILATPATH = "Citation_Tracker_File_Path";

	protected ExecutorWorker(String name) {
		super(name);
	}

	@Override
	public void execute() {
		ParameterInfo param = (ParameterInfo) getData(EXECUTE_PARAMETER);
		// ConsoleOut.getInsance().info(param.toString());
		try {
			job = JobFactory.createExecuteJob(param);
			job.execute();
		} catch (Exception e) {
			ConsoleOut.getInsance().error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public void finished() {
		executor = null;
		ExecuteCitationTraceker ect = (ExecuteCitationTraceker) getData(PARENT_SWT);
		if (ect != null) {
			this.setData(RESULT_FILATPATH, job.getFilePath());
			ect.finished(this);
		}
	}

	@Override
	public void closeResource() {
		// if(job!=null){
		// job.close();
		// }
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		long b = System.currentTimeMillis();
		this.executor = new WorkerThread(this);
		ExecutorStatus finished = null;
		try {
			ParameterInfo param = (ParameterInfo) getData(EXECUTE_PARAMETER);
			if (param == null) {
				return Status.CANCEL_STATUS;
			}
			this.executor.start();

			while ((finished = this.executor.getFinished()) == null) {
				if (monitor.isCanceled()) {
					setCancel(true);
					/* 작업이 취소되었다면. */
					executor.interrupt();
					executor.stop();
					ConsoleOut.getInsance().warn("현재 작업을 취소하였습니다.");
					ConsoleOut.getInsance().cancel();
					/* 작업이 취소되었다면 해당 쓰레드를 종료한다. */
					return Status.CANCEL_STATUS;
				}

				if (finished == ExecutorStatus.CANCEL || finished == ExecutorStatus.EXCEPTION) {
					cancel();
				} else {
					try {
						synchronized (this) {
							wait();
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			// lock.release();
		}

		if (finished == ExecutorStatus.COMPLETE) {
			long a = System.currentTimeMillis();
			ConsoleOut.getInsance().info("분석 시간 : " + (a - b) + "ms");
			ConsoleOut.getInsance().info("분석 작업이 완료되었습니다.");
			setCancel(false);
			return Status.OK_STATUS;
		} else {
			ConsoleOut.getInsance().warn("분석 작업이 취소되었습니다.");
			return Status.CANCEL_STATUS;
		}
	}

}
