/**
 * 
 */
package kr.co.topquadrant.citationTracker.worker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.jobs.Job;

/**
 * 분석에서 사용할 분석 Worker<br>
 * 
 * JobExecutor에서 동작한다.<br>
 * 
 * @author coreawin
 * @sinse 2012. 8. 8.
 * @version 1.0
 * 
 *          <pre>
 * 2012. 8. 8. : 최초 작성
 * </pre>
 */
public abstract class Worker extends Job {

	/**
	 * Job 관련 메시지 정의 클래스
	 * 
	 * @author coreawin
	 * 
	 */
	public class Message {
		public static final String ERROR_INSTANCE_NULL = "인스턴스 이름이 누락되었습니다. ";
		public static final String ERROR_ALREADY_RUNNING = "해당 실행 Job이 이미 실행중입니다. 실행중인 Job을 다시 실행할 수 없습니다. ";
	}

	public enum AnalysisWorkerStatus {
		RUNNING, CANCEL, COMPLETE;
	}

	/**
	 * 현재 Worker의 취소 여부 Flag를 나타낸다.
	 */
	protected AtomicReference<Boolean> cancelFlag = new AtomicReference<Boolean>(false);
	

	/**
	 * 사용자 정의 데이터.
	 */
	private ConcurrentHashMap<String, Object> customData = new ConcurrentHashMap<String, Object>();

	/**
	 * 작업 실행자.
	 */
	WorkerThread executor = null;

	/**
	 * @param name
	 */
	protected Worker(String name) {
		super(name);
	}

	/**
	 * 취소 여부 플래그.
	 * 
	 * @return 현재 작업이 취소 되었다면 true를 리턴한다.
	 */
	public boolean isCancel() {
		return cancelFlag.get();
	}

	/**
	 * 취소 플래그를 설정한다.<br>
	 * 
	 * @param b
	 */
	public void setCancel(boolean b) {
		cancelFlag.set(b);
	}

	/**
	 * 사용자 데이터를 설정한다.
	 * 
	 * @param k
	 *            Key
	 * @param obj
	 *            데이터.
	 */
	public void setData(String k, Object obj) {
		this.customData.put(k, obj);
	}

	/**
	 * 사용자 데이터를 얻는다.
	 * 
	 * @param k
	 *            Key
	 * @return
	 */
	public Object getData(String k) {
		return this.customData.get(k);
	}

	/**
	 * 분석 작업이 이루어져야 할 작업을 작성한다.<br>
	 */
	public abstract void execute();

	/**
	 * execute() 작업이 끝난후 실행할 작업을 작성한다.<br>
	 */
	public abstract void finished();

	public abstract void closeResource();
}
