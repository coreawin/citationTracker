/**
 * 
 */
package kr.co.topquadrant.citationTracker.worker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.jobs.Job;

/**
 * �м����� ����� �м� Worker<br>
 * 
 * JobExecutor���� �����Ѵ�.<br>
 * 
 * @author coreawin
 * @sinse 2012. 8. 8.
 * @version 1.0
 * 
 *          <pre>
 * 2012. 8. 8. : ���� �ۼ�
 * </pre>
 */
public abstract class Worker extends Job {

	/**
	 * Job ���� �޽��� ���� Ŭ����
	 * 
	 * @author coreawin
	 * 
	 */
	public class Message {
		public static final String ERROR_INSTANCE_NULL = "�ν��Ͻ� �̸��� �����Ǿ����ϴ�. ";
		public static final String ERROR_ALREADY_RUNNING = "�ش� ���� Job�� �̹� �������Դϴ�. �������� Job�� �ٽ� ������ �� �����ϴ�. ";
	}

	public enum AnalysisWorkerStatus {
		RUNNING, CANCEL, COMPLETE;
	}

	/**
	 * ���� Worker�� ��� ���� Flag�� ��Ÿ����.
	 */
	protected AtomicReference<Boolean> cancelFlag = new AtomicReference<Boolean>(false);
	

	/**
	 * ����� ���� ������.
	 */
	private ConcurrentHashMap<String, Object> customData = new ConcurrentHashMap<String, Object>();

	/**
	 * �۾� ������.
	 */
	WorkerThread executor = null;

	/**
	 * @param name
	 */
	protected Worker(String name) {
		super(name);
	}

	/**
	 * ��� ���� �÷���.
	 * 
	 * @return ���� �۾��� ��� �Ǿ��ٸ� true�� �����Ѵ�.
	 */
	public boolean isCancel() {
		return cancelFlag.get();
	}

	/**
	 * ��� �÷��׸� �����Ѵ�.<br>
	 * 
	 * @param b
	 */
	public void setCancel(boolean b) {
		cancelFlag.set(b);
	}

	/**
	 * ����� �����͸� �����Ѵ�.
	 * 
	 * @param k
	 *            Key
	 * @param obj
	 *            ������.
	 */
	public void setData(String k, Object obj) {
		this.customData.put(k, obj);
	}

	/**
	 * ����� �����͸� ��´�.
	 * 
	 * @param k
	 *            Key
	 * @return
	 */
	public Object getData(String k) {
		return this.customData.get(k);
	}

	/**
	 * �м� �۾��� �̷������ �� �۾��� �ۼ��Ѵ�.<br>
	 */
	public abstract void execute();

	/**
	 * execute() �۾��� ������ ������ �۾��� �ۼ��Ѵ�.<br>
	 */
	public abstract void finished();

	public abstract void closeResource();
}
