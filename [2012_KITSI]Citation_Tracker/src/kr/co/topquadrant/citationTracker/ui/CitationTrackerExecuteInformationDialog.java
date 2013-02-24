package kr.co.topquadrant.citationTracker.ui;

import kr.co.topquadrant.citationTracker.resource.definition.Definition.MainUILabelText;
import kr.co.topquadrant.citationTracker.resource.definition.Definition.MessageLabelText;

import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class CitationTrackerExecuteInformationDialog extends Dialog {

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public CitationTrackerExecuteInformationDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setImage(Display.getDefault().getSystemImage(SWT.ICON_WORKING));
		lblNewLabel.setBounds(10, 10, 38, 40);

		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setBounds(54, 10, 380, 40);
		lblNewLabel_1.setText(MessageLabelText.CITATION_TRACKER_EXECUTE_WORKING_MESSAGE);

		return container;
	}

	@Override
	protected void cancelPressed() {
		boolean cancelFlag = MessageDialogUtil.getQuestion(getShell(),
				MessageLabelText.CANCEL_EXECUTE_CITATION_TRACKER_MESSAGE);
		if (!cancelFlag) {
			return;
		} else {
			setReturnCode(CANCEL);
			super.close();
		}
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, MainUILabelText.CITATION_TRACKER_EXECUTE_CANCLE_BUTTON, false);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		setShellStyle(SWT.APPLICATION_MODAL | SWT.CLOSE);
		newShell.setText(MainUILabelText.CITATION_TRACEKR_EXECUTE_WORKING_DIALOG_TITLE);
		newShell.setImage(Display.getDefault().getSystemImage(SWT.ICON_WORKING));
	}

	@Override
	protected ShellListener getShellListener() {

		ShellAdapter adapter = new ShellAdapter() {

			@Override
			public void shellClosed(ShellEvent e) {
				e.doit = false;
				boolean cancelFlag = MessageDialogUtil.getQuestion(getShell(),
						MessageLabelText.CANCEL_EXECUTE_CITATION_TRACKER_MESSAGE);
				if (!cancelFlag) {
					return;
				} else {
					setReturnCode(CANCEL);
					close();
				}
			}

		};

		return adapter;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(507, 146);
	}
}
