package kr.co.topquadrant.citationTracker;

import kr.co.topquadrant.citationTracker.ui.ClusterTrackerMainUI;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Executor {

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = new Display();
			Shell shell = new Shell(display);
			ClusterTrackerMainUI window = new ClusterTrackerMainUI(shell);
			window.setBlockOnOpen(true);
			window.open();
			display.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
