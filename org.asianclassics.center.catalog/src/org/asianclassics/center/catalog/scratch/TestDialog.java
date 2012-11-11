package org.asianclassics.center.catalog.scratch;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TestDialog extends Dialog {

	protected int result;
	protected Shell shell;
	private Button btnOpenAnotherDialog;
	private Button btnOk;
	private Button btnCancel;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public TestDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public int open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		
		btnOpenAnotherDialog = new Button(shell, SWT.NONE);
		btnOpenAnotherDialog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				openDialog();
			}
		});
		btnOpenAnotherDialog.setBounds(10, 10, 241, 25);
		btnOpenAnotherDialog.setText("Open another dialog");
		
		btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				result = 1;
				shell.close();
			}
		});
		btnOk.setBounds(10, 68, 75, 25);
		btnOk.setText("OK");
		
		btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				result = 0;
				shell.close();
			}
		});
		btnCancel.setBounds(176, 68, 75, 25);
		btnCancel.setText("Cancel");

	}
	
	private void openDialog() {
		TestDialog dlg = new TestDialog(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dlg.open();
	}

}
