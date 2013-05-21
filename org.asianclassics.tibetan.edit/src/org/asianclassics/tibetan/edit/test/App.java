package org.asianclassics.tibetan.edit.test;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

public class App {

	TestHinting test;
//	TestTibetan test;
	
	protected static Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			App window = new App();
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
		test.init();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
//		System.out.println("Goodbye!");
		SWTResourceManager.dispose();
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		shell.setSize(524, 462);
		test = new TestHinting(shell, SWT.NONE);
		
//		shell.setSize(1600, 300);
//		test = new TestTibetan(shell, SWT.NONE);

	}

}
