package kr.co.topquadrant.citationTracker.ui;

import java.util.Calendar;

import kr.co.topquadrant.citationTracker.console.ConsoleOut;
import kr.co.topquadrant.citationTracker.data.ParameterInfo;
import kr.co.topquadrant.citationTracker.job.ExecuteCitationTraceker;
import kr.co.topquadrant.citationTracker.resource.definition.Definition.MainUIComboItemText;
import kr.co.topquadrant.citationTracker.resource.definition.Definition.MainUILabelText;
import kr.co.topquadrant.citationTracker.resource.definition.Definition.MessageLabelText;
import kr.co.topquadrant.citationTracker.ui.event.EndYearEvent;
import kr.co.topquadrant.citationTracker.ui.event.StartYearEvent;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class ClusterTrackerMainUI extends ApplicationWindow {
	private Text text;

	public enum CitationDirection {
		Forward, Backward
	}

	public enum CitationTrackerOption {
		Inside, Outside
	}

	public enum ExecuteType {
		ExeType1, ExeType2, ExeType3, ExeType4, ExeType5, ExeType6, ExeType7
	}

	StatusLineManager statusLineManager = new StatusLineManager();

	Action openExcelFileAction = new OpenButtoAction(statusLineManager);
	private ActionContributionItem aci = new ActionContributionItem(openExcelFileAction);

	private Combo endYearCombo;

	private Combo startYearCombo;

	private TextViewer textViewer;

	private Button btnExecuteButton;

	/**
	 * Create the application window,
	 */
	public ClusterTrackerMainUI(Shell parentShell) {
		super(parentShell);
		createActions();
		addCoolBar(SWT.FLAT);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Main 화면을 표시 Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridData grid_container = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		container.setLayoutData(grid_container);
		GridLayout gl_container = new GridLayout(2, false);
		container.setLayout(gl_container);

		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		aci.fill(container);
		Button openButton = (Button) aci.getWidget();
		GridData gd_btnOpen = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);

		gd_btnOpen.widthHint = 80;
		gd_btnOpen.heightHint = 25;
		openButton.setLayoutData(gd_btnOpen);

		Composite compCitationTrackerSetting = new Composite(container, SWT.NONE);
		GridLayout gl_compCitationTrackerSetting = new GridLayout(2, false);
		gl_compCitationTrackerSetting.marginHeight = 0;
		gl_compCitationTrackerSetting.marginWidth = 0;
		compCitationTrackerSetting.setLayout(gl_compCitationTrackerSetting);
		GridData gd_compCitationTrackerSetting = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1);
		gd_compCitationTrackerSetting.heightHint = 100;
		compCitationTrackerSetting.setLayoutData(gd_compCitationTrackerSetting);

		Group grpCitationDirection = new Group(compCitationTrackerSetting, SWT.NONE);
		grpCitationDirection.setLayout(new GridLayout(2, false));
		GridData gd_grpCitationDirection = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_grpCitationDirection.heightHint = 30;
		gd_grpCitationDirection.widthHint = 170;
		grpCitationDirection.setLayoutData(gd_grpCitationDirection);
		grpCitationDirection.setText(MainUILabelText.CITATION_TRACKER_CITATION_DIRECTION_GROUP);

		Button btnForwardRadio = new Button(grpCitationDirection, SWT.RADIO);
		btnForwardRadio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		btnForwardRadio.setText(MainUILabelText.CITATION_TRACKER_CITATION_DIRECTION_FORWARD_RADIO);
		btnForwardRadio.setSelection(true);

		Button btnBackwardRadio = new Button(grpCitationDirection, SWT.RADIO);
		btnBackwardRadio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		btnBackwardRadio.setText(MainUILabelText.CITATION_TRACKER_CITATION_DIRECTION_BACKWARD_RADIO);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());

		Group grpYear = new Group(compCitationTrackerSetting, SWT.NONE);
		grpYear.setLayout(new GridLayout(3, false));
		GridData gd_grpYear = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_grpYear.heightHint = 76;
		grpYear.setLayoutData(gd_grpYear);
		grpYear.setText(MainUILabelText.CITATION_TRACKER_YEAR_GROUP);

		Button btnConfirmCheck = new Button(grpYear, SWT.CHECK);
		btnConfirmCheck.setText(MainUILabelText.CITATION_TRACKER_YEAR_CONFIRM_RADIO);
		GridData gd_btnRadioButton_4 = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		gd_btnRadioButton_4.widthHint = 68;
		btnConfirmCheck.setLayoutData(gd_btnRadioButton_4);
		btnConfirmCheck.setSelection(false);

		Label lblStartYear = new Label(grpYear, SWT.NONE);
		lblStartYear.setAlignment(SWT.RIGHT);
		lblStartYear.setText(MainUILabelText.CITATION_TRACKER_YEAR_START_YEAR_LABEL);
		startYearCombo = new Combo(grpYear, SWT.NONE);
		startYearCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		startYearCombo.setItems(MainUIComboItemText.CITATION_TRACKER_YEAR_DATA);
		startYearCombo.setText(String.valueOf(cal.get(Calendar.YEAR)));
		int startYearItemEndIndex = startYearCombo.getItemCount() - 1;
		startYearCombo.select(startYearItemEndIndex - 5);
		Label lblEndYear = new Label(grpYear, SWT.NONE);
		lblEndYear.setText(MainUILabelText.CITATION_TRACKER_YEAR_END_YEAR_LABEL);

		endYearCombo = new Combo(grpYear, SWT.NONE);
		GridData gd_combo_2 = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_combo_2.widthHint = 66;
		endYearCombo.setLayoutData(gd_combo_2);
		endYearCombo.setItems(MainUIComboItemText.CITATION_TRACKER_YEAR_DATA);
		endYearCombo.setText(String.valueOf(cal.get(Calendar.YEAR)));
		int endYearItemEndIndex = endYearCombo.getItemCount() - 1;
		endYearCombo.select(endYearItemEndIndex);
		if (!btnConfirmCheck.getSelection()) {
			startYearCombo.setEnabled(false);
			endYearCombo.setEnabled(false);
		} else {
			startYearCombo.setEnabled(true);
			endYearCombo.setEnabled(true);
		}

		startYearCombo.addSelectionListener(new StartYearEvent(endYearCombo, startYearCombo));
		startYearCombo.addKeyListener(new StartYearEvent(endYearCombo, startYearCombo));
		endYearCombo.addSelectionListener(new EndYearEvent(endYearCombo, startYearCombo));
		endYearCombo.addKeyListener(new EndYearEvent(endYearCombo, startYearCombo));

		btnConfirmCheck.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() instanceof Button) {
					Button btn = (Button) e.getSource();
					if (btn.getSelection()) {
						startYearCombo.setEnabled(true);
						endYearCombo.setEnabled(true);
					} else {
						startYearCombo.setEnabled(false);
						endYearCombo.setEnabled(false);
					}
				}
			}

		});

		Group grpOption = new Group(compCitationTrackerSetting, SWT.NONE);
		GridData gd_grpOption = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_grpOption.heightHint = 30;
		grpOption.setLayoutData(gd_grpOption);
		grpOption.setText(MainUILabelText.CITATION_TRACKER_OPTION_GROUP);
		grpOption.setLayout(new GridLayout(2, false));

		Button btnInsideRadio = new Button(grpOption, SWT.RADIO);
		btnInsideRadio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		btnInsideRadio.setText(MainUILabelText.CITATION_TRACKER_OPTION_INSIDE_RADIO);
		btnInsideRadio.setSelection(true);

		Button btnOutsideRadio = new Button(grpOption, SWT.RADIO);
		btnOutsideRadio.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		btnOutsideRadio.setText(MainUILabelText.CITATION_TRACKER_OPTION_OUTSIDE_RADIO);

		Group grpExecute = new Group(container, SWT.NONE);
		grpExecute.setText(MainUILabelText.CITATION_TRACKER_EXECUTE_GROUP);
		GridLayout gl_grpExecute = new GridLayout(2, false);
		gl_grpExecute.marginWidth = 5;
		gl_grpExecute.marginHeight = 5;
		grpExecute.setLayout(gl_grpExecute);
		grpExecute.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		Combo executeTypeCombo = new Combo(grpExecute, SWT.READ_ONLY);
		executeTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		executeTypeCombo.setItems(MainUIComboItemText.CITATION_TRACKER_EXECUTE_TYPE);
		executeTypeCombo.select(0);

		Group grpConsoleLog = new Group(container, SWT.NONE);
		grpConsoleLog.setText(MainUILabelText.CITATION_TRACKER_CONSOLE_LOG_GROUP);
		GridLayout gl_grpConsoleLog = new GridLayout(1, false);
		gl_grpConsoleLog.marginHeight = 0;
		gl_grpConsoleLog.marginWidth = 0;
		grpConsoleLog.setLayout(gl_grpConsoleLog);
		GridData gd_grpConsoleLog = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1);
		gd_grpConsoleLog.heightHint = 240;
		grpConsoleLog.setLayoutData(gd_grpConsoleLog);

		textViewer = new TextViewer(grpConsoleLog, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP | SWT.READ_ONLY);
		StyledText styledText = textViewer.getTextWidget();
		styledText.setMenu(createLogConsoleContextMenu(textViewer));
		GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_styledText.heightHint = 224;
		styledText.setLayoutData(gd_styledText);

		Action executeAction = new ExecuteButtoAction(statusLineManager, btnForwardRadio, btnInsideRadio,
				btnConfirmCheck, startYearCombo, endYearCombo, executeTypeCombo, text, textViewer);
		ActionContributionItem executeAci = new ActionContributionItem(executeAction);
		executeAci.fill(grpExecute);
		btnExecuteButton = (Button) executeAci.getWidget();
		GridData gd_btnExecuteButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnExecuteButton_1.widthHint = 80;
		gd_btnExecuteButton_1.heightHint = 25;
		btnExecuteButton.setLayoutData(gd_btnExecuteButton_1);

		return container;
	}

	/**
	 * ConsoleLog 부분의 대한 Context Menu를 뿌려준다.
	 * 
	 * @param logConsoleViewer
	 * @return
	 */
	private Menu createLogConsoleContextMenu(TextViewer logConsoleViewer) {
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(logConsoleViewer.getControl());
		menuManager.add(new LogConsoleAction(statusLineManager));
		return contextMenu;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {

	}

	/**
	 * Create the status line manager.
	 * 
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		return statusLineManager;
	}

	/**
	 * 현재 화면 대한 설정정보를 세팅한다. Configure the shell.
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(MainUILabelText.PROGRAM_TITLE);
		newShell.setImage(SWTResourceManager.getImage(ClusterTrackerMainUI.class,
				"/kr/co/topquadrant/citationTracker/img/wtp_icon_x16.gif"));
	}

	/**
	 * 최초 실행 화면에 대한 크기를 리턴한다. Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(549, 480);
	}

	/**
	 * 
	 * Open 버튼이 클릭이 되어졌을때 발생되는 Action을 정의한 클래스
	 * 
	 * @author 이관재
	 * @sinse 2012. 11. 28.
	 * @version 1.0
	 * @history 2012. 11. 28. : 최초작성
	 */
	public class OpenButtoAction extends Action {
		private StatusLineManager slm;

		public OpenButtoAction(StatusLineManager slm) {
			super(MainUILabelText.CITATION_TRACKER_EXCEL_FIEL_OPEN_BUTTON, AS_PUSH_BUTTON);
			this.slm = slm;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.Action#run() Open Action이 발생되었을때 처리되는
		 * 이벤트
		 */
		@Override
		public void run() {
			slm.setMessage(MessageLabelText.STATUS_EXCEL_FILE_OPEN_MESSAGE);
			FileDialog excelFileDialog = new FileDialog(getShell(), SWT.OPEN);
			excelFileDialog.setText(MainUILabelText.CITATION_TRACKER_EXCEL_FIEL_OPEN_BUTTON);
			excelFileDialog.setFilterExtensions(MainUIComboItemText.CITATION_TRACKER_EXCEL_FILE_EXTENSION);
			excelFileDialog.setFilterPath(Env.HOME);
			String filePath = excelFileDialog.open();
			if (filePath == null) {
				slm.setMessage("");
				return;
			}
			text.setText(filePath.trim());
			slm.setMessage("");
		}

	}

	/**
	 * 실행 버튼을 클릭했을때 발생되는 Action 정의
	 * 
	 * @author 이관재
	 * @sinse 2012. 11. 28.
	 * @version 1.0
	 * @history 2012. 11. 28. : 최초작성
	 */
	public class ExecuteButtoAction extends Action {
		private StatusLineManager slm;
		private Button btnForwardRadio;
		private Combo startYearCombo;
		private Combo endYearCombo;
		private Button btnConfirmCheck;
		private Button btnInsideRadio;
		private Text execlFilePathText;
		private TextViewer consoleViewer;
		private Combo executeTypeCombo;

		public ExecuteButtoAction(StatusLineManager slm, Button btnForwardRadio, Button btnInsideButton,
				Button btnConfirmCheck, Combo startYearCombo, Combo endYearCombo, Combo executeTypeCombo,
				Text execlFilePathText, TextViewer textViewer) {
			super(MainUILabelText.CITATION_TRACKER_EXECUTE_EXECUTE_BUTTON, AS_PUSH_BUTTON);
			this.slm = slm;
			this.btnForwardRadio = btnForwardRadio;
			this.startYearCombo = startYearCombo;
			this.endYearCombo = endYearCombo;
			this.btnConfirmCheck = btnConfirmCheck;
			this.btnInsideRadio = btnInsideButton;
			this.executeTypeCombo = executeTypeCombo;
			this.execlFilePathText = execlFilePathText;
			this.consoleViewer = textViewer;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.Action#run()</br> Action이 발생되었을때 처리된다.
		 */
		@Override
		public void run() {
			slm.setMessage(MessageLabelText.STATUS_CITATION_TRACKER_EXECUTE_MESSAGE);
			String citation_Tracker_Name;
			String excelFilePath;
			if ("".equals(execlFilePathText.getText())) {
				MessageDialogUtil.getWarningMessageDialog(getShell(),
						MessageLabelText.WARNING_NONE_SELECTION_EXCEL_FILE_MESSAGE);
				slm.setMessage("");
				return;
			} else {
				excelFilePath = execlFilePathText.getText();
				citation_Tracker_Name = MainUILabelText.CITATION_TRACKER_EXECUTE_JOB_NAME;
			}

			CitationDirection directionType;

			if (btnForwardRadio.getSelection()) {
				directionType = CitationDirection.Forward;
			} else {
				directionType = CitationDirection.Backward;
			}

			CitationTrackerOption trackerType;
			if (btnInsideRadio.getSelection()) {
				trackerType = CitationTrackerOption.Inside;
			} else {
				trackerType = CitationTrackerOption.Outside;
			}

			int startYear = -1;
			int endYear = -1;
			if (btnConfirmCheck.getSelection()) {
				startYear = Integer.valueOf(this.startYearCombo.getText());
				endYear = Integer.valueOf(this.endYearCombo.getText());
			} else {
				startYear = Integer.valueOf(this.startYearCombo.getItem(0));
				endYear = Integer.valueOf(this.endYearCombo.getItem(endYearCombo.getItemCount() - 1));
			}

			ExecuteType selectionType = null;
			int i = 0;
			for (ExecuteType type : ExecuteType.values()) {
				if (i == executeTypeCombo.getSelectionIndex()) {
					selectionType = type;
					break;
				}
				i++;
			}
			// 임시코드
			if (selectionType == null) {
				MessageDialogUtil.getWarningMessageDialog(getShell(),
						MessageLabelText.WARNING_NONE_EXECUTE_TYPE_SELECTION);
				return;
			}

			consoleViewer.setData(MessageLabelText.WORKING_SET_DATA_JOB_NAME, citation_Tracker_Name);
			ParameterInfo param = new ParameterInfo(directionType, trackerType, startYear, endYear, excelFilePath,
					selectionType, consoleViewer, citation_Tracker_Name);
			ExecuteCitationTraceker executeTraceker = new ExecuteCitationTraceker(getShell(), btnExecuteButton,
					citation_Tracker_Name, param, selectionType);
			slm.setMessage("");
		}
	}

	/**
	 * 
	 * Clear Log Console를 선택 했을때 로그창을 클리어하는 Action 정의
	 * 
	 * @author 이관재
	 * @sinse 2012. 11. 28.
	 * @version 1.0
	 * @history 2012. 11. 28. : 최초작성
	 */
	public class LogConsoleAction extends Action {
		private StatusLineManager slm;

		public LogConsoleAction(StatusLineManager slm) {
			super(MainUILabelText.CITATION_TRACKER_CONSOLE_LOG_CLEAR_MENU_ITEM);
			this.slm = slm;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.Action#run() </br> * Console Log를 비운다.
		 */
		@Override
		public void run() {
			ConsoleOut.getInsance().clear();
		}
	}

}
