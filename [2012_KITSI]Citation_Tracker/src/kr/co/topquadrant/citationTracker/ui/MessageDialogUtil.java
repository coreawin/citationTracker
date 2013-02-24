package kr.co.topquadrant.citationTracker.ui;

import kr.co.topquadrant.citationTracker.resource.definition.Definition.MessageBoxDialogTitleText;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class MessageDialogUtil {

	public static void getWarningMessageDialog(Shell parent, String message) {
		MessageDialog.openWarning(parent, MessageBoxDialogTitleText.WARNING_MESSAGE_DITALOG_TITLE, message);
	}

	public static boolean getQuestion(Shell parent, String message) {
		return MessageDialog.openQuestion(parent, MessageBoxDialogTitleText.QUESTION_MESSAGE_DITALOG_TITLE, message);
	}

	public static boolean getConfirm(Shell parent, String message) {
		return MessageDialog.openConfirm(parent, MessageBoxDialogTitleText.CONFIRM_MESSAGE_DITALOG_TITLE, message);
	}

	public static void getInformation(Shell parent, String message) {
		MessageDialog.openInformation(parent, MessageBoxDialogTitleText.INFORMATION_MESSAGE_DITALOG_TITLE, message);
	}

	public static void getError(Shell parent, String message) {
		MessageDialog.openError(parent, MessageBoxDialogTitleText.ERROR_MESSAGE_DITALOG_TITLE, message);
	}

}
