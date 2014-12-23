package org.asianclassics.text.edit.test;

import org.asianclassics.text.edit.AcipEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;

public class AcipTextEditTestApp {

	protected Shell shell;
	protected AcipEditor editor;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AcipTextEditTestApp window = new AcipTextEditTestApp();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("ACIP Editor Test");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		editor = new AcipEditor(shell);

		addTestText();
		
	}

	
	protected void addTestText() {
//		String wrkTxt = "If the doors of perception were cleansed\nevery thing would appear to man as it is, infinite.\nFor man has closed himself up,\ntill he sees all things thro'\nnarow chinks of his cavern.";
//		String refTxt = wrkTxt;
//		String refTxt = "the doors of perception";
		String refTxt = "line one\nline two\nline three";
		String wrkTxt = "lineone\nlinne twoextra\nlinf zzree";

		editor.setReferenceText(refTxt);
		editor.setWorkingText(wrkTxt);
	}
}
