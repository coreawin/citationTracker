package kr.co.topquadrant.citationTracker.console;

import java.util.HashMap;
import java.util.Map;

import kr.co.topquadrant.citationTracker.resource.definition.Definition;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.widgets.Display;

/**
 * Ŭ������ �м� �׸� �α� â�� �����͸� ����Ѵ�.
 * 
 * @author coreawin
 * 
 */
public class ConsoleOut {

	public enum LEVEL {
		INFO, ERROR, WARN;
	};

	private static ConsoleOut instance = null;

	private static Map<String, ConsoleOut> pool = new HashMap<String, ConsoleOut>();

	Display display = null;
	TextViewer viewer = null;

	private ConsoleBuffer buffer = new ConsoleBuffer();

	ConsoleOut(TextViewer viewer) {
		this.viewer = viewer;
		if (viewer != null) {
			this.display = viewer.getTextWidget().getDisplay();
		}
	}

	private static String id;

	public static ConsoleOut createInstance(String name, TextViewer textViewer) {
		id = name;
		instance = pool.get(name);
		if (instance == null) {
			instance = new ConsoleOut(textViewer);
			// System.out.println("ConsoleOut " + name);
			pool.put(name, instance);
		}
		return instance;
	}

	/**
	 * ��ϵ� �ܼ� �α� ����ڸ� �����Ѵ�.<br>
	 * 
	 * @param name
	 * @return
	 */
	public static ConsoleOut removeInstance(String name) {
		// System.out.println("ConsoleOut remove " + name);
		return pool.remove(name);
	}

	/**
	 * �ݵ�� createInstance�� ����Ѵ�.<br>
	 * 
	 * @return
	 */
	@Deprecated
	public static ConsoleOut getInsance(String name) {
		instance = pool.get(name);
		if (instance == null)
			throw new RuntimeException("�ν��Ͻ��� �������� �ʾҽ��ϴ�.");
		return instance;
	}

	/**
	 * 
	 * @return
	 */
	public static ConsoleOut getInsance() {
		String name = Definition.MainUILabelText.CITATION_TRACKER_EXECUTE_JOB_NAME;
		instance = pool.get(name);
		if (instance == null)
			throw new RuntimeException("�ν��Ͻ��� �������� �ʾҽ��ϴ�.");
		return instance;
	}

	public void print(String x) {
		buffer.setText(x);
		outputConsole();
	}

	public void println(LEVEL level, String x) {
		buffer.setTextLine(level, x);
		outputConsole();
	}

	public void clear() {
		buffer.clearBuffer();
		outputConsole();
	}

	public ConsoleOut info(String x) {
		println(LEVEL.INFO, x);
		return this;
	}

	public ConsoleOut warn(String x) {
		println(LEVEL.WARN, x);
		return this;
	}

	public ConsoleOut error(String x) {
		println(LEVEL.ERROR, x);
		return this;
	}

	/**
	 * �������� �����Ѵ�.
	 * 
	 * @param x
	 *            0~100 ������ ��.
	 */
	// public void setProgressSelection(final int x) {
	// if (display != null) {
	// display.asyncExec(new Runnable() {
	// public void run() {
	// ProgressBar pb = (ProgressBar) viewer
	// .getData(Definition.LogConsoleComponentID.LOG_CONSOLE_COMPONENT_PROGRESS_BAR);
	// pb.setSelection(x);
	// }
	// });
	// }
	// }

	/**
	 * �������� �ʱ�ȭ �Ѵ�.
	 */
	public void setProgressSelectionInit() {
		// setProgressSelection(0);
	}

	private class Print extends Thread {
		public void run() {
			IDocument document = viewer.getDocument();
			if (document == null) {
				document = new Document();
			}
			document.set(buffer.getText());
			viewer.setDocument(document);
		}
	}

	private Print p = null;

	private void outputConsole() {
		if (display != null) {
			p = new Print();
			display.asyncExec(p);
		}
	}

	public void cancel() {
		if (p != null) {
			p.interrupt();
			p.stop();
		}
	}
}
