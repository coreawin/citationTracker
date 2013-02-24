package kr.co.topquadrant.citationTracker.ui.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;

public class StartYearEvent extends SelectionAdapter implements KeyListener {

	private Combo endYearCombo;
	private Combo startYearCombo;

	public StartYearEvent(Combo endYearCombo, Combo startYearCombo) {
		this.endYearCombo = endYearCombo;
		this.startYearCombo = startYearCombo;

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		setEndYear();
	}

	private void setEndYear() {
		String endYearText = endYearCombo.getText();
		String startYearText = startYearCombo.getText();
		int maxYear = Integer.valueOf(startYearCombo.getItem(startYearCombo.getItemCount() - 1));
		int endYear = Integer.valueOf(endYearText).intValue();
		int startYear = Integer.valueOf(startYearText).intValue();
		if (startYear > maxYear) {
			startYear = maxYear;
			startYearCombo.setText(String.valueOf(maxYear));
		}
		if (startYear > endYear) {
			endYearCombo.setText(startYearText);
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
			setEndYear();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
