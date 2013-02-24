package kr.co.topquadrant.citationTracker.job;

import java.io.File;
import java.io.IOException;

import kr.co.topquadrant.citationTracker.console.ConsoleOut;
import kr.co.topquadrant.citationTracker.data.ParameterInfo;
import kr.co.topquadrant.citationTracker.resource.definition.Definition.MainUILabelText;
import kr.co.topquadrant.citationTracker.resource.definition.Definition.MessageLabelText;
import kr.co.topquadrant.citationTracker.ui.CitationTrackerExecuteInformationDialog;
import kr.co.topquadrant.citationTracker.ui.ClusterTrackerMainUI.ExecuteType;
import kr.co.topquadrant.citationTracker.ui.MessageDialogUtil;
import kr.co.topquadrant.citationTracker.worker.ExecuteWorkerSample;
import kr.co.topquadrant.citationTracker.worker.ExecutorWorker;
import kr.co.topquadrant.citationTracker.worker.Worker;
import kr.co.topquadrant.citationTracker.worker.WorkerDispatcher;
import kr.co.topquadrant.citationTracker.worker.WorkerFactory;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

public class ExecuteCitationTraceker {

	private Button executeButton;
	private Shell shell;
	private ParameterInfo param;
	private String tracekerName;
	private ExecuteType type;
	private CitationTrackerExecuteInformationDialog informationDialog;

	private static final String WIN_ID = MainUILabelText.CITATION_TACKER_OS_WINDOWS;
	private static final String WIN_PATH = MainUILabelText.CITATION_TACKER_OS_WINDOWS_RUN_DLL;
	private static final String WIN_FLAG = MainUILabelText.CITATION_TRACKER_OS_WINDOWS_DLL_PROTOCOL;

	public ExecuteCitationTraceker(Shell shell, Button executeButton, String tracekerName, ParameterInfo param,
			ExecuteType type) {
		this.shell = shell;
		this.executeButton = executeButton;
		this.tracekerName = tracekerName;
		this.param = param;
		this.type = type;

		executeButton.setText(MainUILabelText.CITATION_TRACKER_EXECUTE_RUNNING_BUTTON);
		executeButton.setEnabled(false);
		execute();
		informationDialog = new CitationTrackerExecuteInformationDialog(shell);
		int iscancel = informationDialog.open();
		if (iscancel == Dialog.CANCEL) {
			cancel(tracekerName);
		}
	}

	public void execute() {
		Worker worker = WorkerFactory.createWorker(type, tracekerName);
		worker.setData(ExecuteWorkerSample.EXECUTE_PARAMETER, param);
		worker.setData(ExecuteWorkerSample.PARENT_SWT, this);
		worker.setData(ExecuteWorkerSample.CONSOLE_LOG,
				ConsoleOut.createInstance(worker.getName(), param.getLogConsole()));

		if (worker.getState() == Job.RUNNING) {
			throw new RuntimeException(Worker.Message.ERROR_ALREADY_RUNNING);
		} else {
			WorkerDispatcher.getInstance().addJob(worker);
		}

	}

	public void finished(final Worker worker) {
		if (!worker.isCancel()) {
			shell.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					informationDialog.close();
					MessageDialogUtil.getInformation(shell, MessageLabelText.CITATION_TRACKER_COMPLETE_MESSAGE);
					executeButton.setText(MainUILabelText.CITATION_TRACKER_EXECUTE_EXECUTE_BUTTON);
					executeButton.setEnabled(true);
					String resultFilePath = String.valueOf(worker.getData(ExecutorWorker.RESULT_FILATPATH));
					if (resultFilePath != null) {
						boolean openResultFile = MessageDialogUtil.getQuestion(shell,
								MessageLabelText.CITATION_TRACKER_RESULT_FILE_OPEN);
						if (openResultFile) {
							if (isWindows()) {
								try {
									String cmd = WIN_PATH + " " + WIN_FLAG + " "
											+ new File(resultFilePath).getCanonicalPath();
									Process p = Runtime.getRuntime().exec(cmd);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			});
			return;
		}
		shell.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				informationDialog.close();
				MessageDialogUtil.getInformation(shell, MessageLabelText.CITATION_TRACKER_CANCEL_MESSAGE);
				executeButton.setText(MainUILabelText.CITATION_TRACKER_EXECUTE_EXECUTE_BUTTON);
				executeButton.setEnabled(true);
			}
		});
	}

	public void cancelFinished(final boolean iscancel) {
		if (!iscancel) {
			shell.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					informationDialog.close();
					MessageDialogUtil.getInformation(shell, MessageLabelText.CITATION_TRACKER_COMPLETE_MESSAGE);
					executeButton.setText(MainUILabelText.CITATION_TRACKER_EXECUTE_EXECUTE_BUTTON);
					executeButton.setEnabled(true);
				}
			});
			return;
		}
		shell.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				informationDialog.close();
				MessageDialogUtil.getInformation(shell, MessageLabelText.CITATION_TRACKER_CANCEL_MESSAGE);
				executeButton.setText(MainUILabelText.CITATION_TRACKER_EXECUTE_EXECUTE_BUTTON);
				executeButton.setEnabled(true);
			}
		});
	}

	public void cancel(String trackerName) {
		boolean iscancel = WorkerDispatcher.getInstance().cancelJob();
		if (iscancel) {
			cancelFinished(iscancel);
		}
	}

	public boolean isWindows() {
		String os = System.getProperty(MainUILabelText.CIATAION_TRACKER_OS_NAME);
		if (os != null && os.indexOf(WIN_ID) != -1) {
			return true;
		}
		return false;
	}
}
