package kr.co.topquadrant.citationTracker.resource.definition;

import java.util.LinkedHashSet;
import java.util.Set;

import com.ibm.icu.util.Calendar;

import kr.co.topquadrant.citationTracker.resource.bundle.SystemResourceBundle;
import kr.co.topquadrant.citationTracker.resource.bundle.ToolResource;

public final class Definition {

	static ToolResource bundle = SystemResourceBundle.getInstance().getResourceBundle();

	/**
	 * Main UI에서 사용되어지는 Button, Label, Group 의 대한 상수를 정의한다.
	 * 
	 * @author 이관재
	 * @sinse 2012. 11. 27.
	 * @version 1.0
	 * @history 2012. 11. 27. : 최초작성
	 */
	public static final class MainUILabelText {
		public static final String PROGRAM_TITLE = bundle.getMessage("program_TItle");
		public static final String CITATION_TRACEKR_EXECUTE_WORKING_DIALOG_TITLE = bundle
				.getMessage("working_Dialog_Title");

		public static final String CITATION_TRACKER_EXECUTE_CANCLE_BUTTON = bundle
				.getMessage("execute_citation_tracker_Cancel");

		public static final String CITATION_TRACKER_EXCEL_FIEL_OPEN_BUTTON = bundle.getMessage("excel_File_Open");
		public static final String CITATION_TRACKER_EXCEL_FIEL_OPEN_FILE_DAILOG_FILTER_PATH = bundle
				.getMessage("user_home");

		/**
		 * CITATION_TRACKER_CITATION_DIRECTION_GROUP
		 */
		public static final String CITATION_TRACKER_CITATION_DIRECTION_GROUP = bundle
				.getMessage("citation_Direction_Group");
		public static final String CITATION_TRACKER_CITATION_DIRECTION_FORWARD_RADIO = bundle
				.getMessage("citation_Direction_Forward");
		public static final String CITATION_TRACKER_CITATION_DIRECTION_BACKWARD_RADIO = bundle
				.getMessage("citation_Direction_Backward");

		public static final String CITATION_TRACKER_OPTION_GROUP = bundle.getMessage("option_Group");

		public static final String CITATION_TRACKER_OPTION_INSIDE_RADIO = bundle.getMessage("option_Inside");

		public static final String CITATION_TRACKER_OPTION_OUTSIDE_RADIO = bundle.getMessage("option_Outside");

		public static final String CITATION_TRACKER_YEAR_GROUP = bundle.getMessage("year_Group");

		public static final String CITATION_TRACKER_YEAR_START_YEAR_LABEL = bundle.getMessage("year_startYear");

		public static final String CITATION_TRACKER_YEAR_END_YEAR_LABEL = bundle.getMessage("year_endYear");

		public static final String CITATION_TRACKER_YEAR_CONFIRM_RADIO = bundle.getMessage("year_Confirm");

		public static final String CITATION_TRACKER_YEAR_NOT_CONFIRM_RADIO = bundle.getMessage("year_Not_Confirm");

		public static final String CITATION_TRACKER_EXECUTE_GROUP = bundle.getMessage("execute_Group");

		public static final String CITATION_TRACKER_EXECUTE_EXECUTE_BUTTON = bundle
				.getMessage("execute_citation_tracker");

		public static final String CITATION_TRACKER_EXECUTE_RUNNING_BUTTON = bundle
				.getMessage("execute_ciatation_tracking");

		public static final String CITATION_TRACKER_EXECUTE_JOB_NAME = bundle
				.getMessage("execute_citation_tracker_Name");

		public static final String CITATION_TRACKER_CONSOLE_LOG_GROUP = bundle.getMessage("log_Console_Group");

		public static final String CITATION_TRACKER_CONSOLE_LOG_CLEAR_MENU_ITEM = bundle
				.getMessage("log_Console_clear");

		public static final String CIATAION_TRACKER_OS_NAME = bundle.getMessage("execute_citation_tracker_OS_name");

		public static final String CITATION_TACKER_OS_WINDOWS = bundle.getMessage("execute_citation_tracker_Windows");

		public static final String CITATION_TACKER_OS_WINDOWS_RUN_DLL = bundle
				.getMessage("execute_citation_tracker_run_dll");

		public static final String CITATION_TRACKER_OS_WINDOWS_DLL_PROTOCOL = bundle
				.getMessage("execute_citation_tracker_FileProtocolHandler");
	}

	/**
	 * Main UI에서 사용되어지는 ComboItem의 대한 상수를 정의한다.
	 * 
	 * @author 이관재
	 * @sinse 2012. 11. 27.
	 * @version 1.0
	 * @history 2012. 11. 27. : 최초작성
	 */
	public static final class MainUIComboItemText {

		public static final String[] CITATION_TRACKER_EXCEL_FILE_EXTENSION = {bundle
				.getMessage("excelFile_FilterExtension")};

		public static final String[] CITATION_TRACKER_EXECUTE_TYPE = { bundle.getMessage("execute_Type1"),
				bundle.getMessage("execute_Type2"), bundle.getMessage("execute_Type3"),
				bundle.getMessage("execute_Type4"), bundle.getMessage("execute_Type5"),
				bundle.getMessage("execute_Type6"), bundle.getMessage("execute_Type7") };

		private static Set<String> yearData = new LinkedHashSet<String>();
		public static final String[] CITATION_TRACKER_YEAR_DATA;

		static {
			String start_year = bundle.getMessage("start_year");
			int start = Integer.valueOf(start_year);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());

			int current_year = cal.get(Calendar.YEAR);
			System.out.println(current_year);
			for (int i = start; i <= current_year; i++) {
				yearData.add(String.valueOf(i));
			}

			CITATION_TRACKER_YEAR_DATA = yearData.toArray(new String[] {});
		}

	}

	/**
	 * MessageBox에서 사용하는 Title 정보에 대해서 상수를 정의 한다.
	 * 
	 * @author 이관재
	 * @sinse 2012. 11. 27.
	 * @version 1.0
	 * @history 2012. 11. 27. : 최초작성
	 */
	public static final class MessageBoxDialogTitleText {

		public static final String WARNING_MESSAGE_DITALOG_TITLE = bundle.getMessage("warning");

		public static final String QUESTION_MESSAGE_DITALOG_TITLE = bundle.getMessage("question");

		public static final String INFORMATION_MESSAGE_DITALOG_TITLE = bundle.getMessage("information");

		public static final String CONFIRM_MESSAGE_DITALOG_TITLE = bundle.getMessage("confirm");

		public static final String ERROR_MESSAGE_DITALOG_TITLE = bundle.getMessage("error");
	}

	/**
	 * Message등에서 사용되어지는 메세지들의 대한 상수를 정의한다.
	 * 
	 * @author 이관재
	 * @sinse 2012. 11. 27.
	 * @version 1.0
	 * @history 2012. 11. 27. : 최초작성
	 */
	public static final class MessageLabelText {

		public static final String STATUS_EXCEL_FILE_OPEN_MESSAGE = bundle.getMessage("status_Open_Excel_File");

		public static final String STATUS_CITATION_TRACKER_EXECUTE_MESSAGE = bundle
				.getMessage("status_Citation_Tracker_Execute");

		public static final String STATUS_YEAR_NOT_CONFIRM_MESSAGE = bundle.getMessage("status_Year_Not_Confirm");

		public static final String STATUS_YEAR_CONFIRM_MESSAGE = bundle.getMessage("status_Year_Confirm");

		public static final String WARNING_NONE_SELECTION_EXCEL_FILE_MESSAGE = bundle
				.getMessage("warning_None_Select_Excel_File");

		public static final String WORKING_SET_DATA_JOB_NAME = bundle.getMessage("working_Job_name_data");

		public static final String CITATION_TRACKER_EXECUTE_WORKING_MESSAGE = bundle
				.getMessage("citation_Tracker_Execute_Working_Message");

		public static final String WARNING_NONE_EXECUTE_TYPE_SELECTION = bundle
				.getMessage("execute_Type_Data_None_Select");

		public static final String CANCEL_EXECUTE_CITATION_TRACKER_MESSAGE = bundle
				.getMessage("execute_Citation_Tracker_Cancel_Message");

		public static final String CITATION_TRACKER_COMPLETE_MESSAGE = bundle
				.getMessage("execute_Citation_Tracker_Complete_Information_Message");
		public static final String CITATION_TRACKER_CANCEL_MESSAGE = bundle
				.getMessage("execute_Citation_Tracker_Cancel_Information_Message");

		public static final String CITATION_TRACKER_RESULT_FILE_OPEN = bundle
				.getMessage("execute_Citation_Tracker_Open_Result_File_Message");
	}

}
