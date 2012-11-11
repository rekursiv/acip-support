package org.asianclassics.center.catalog.entry;

import java.util.logging.Logger;

import org.asianclassics.center.catalog.entry.model.StampRepo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class StampBrowseDialog extends Dialog {

	protected int result;
	protected Shell shell;
	private StampRepo repo;
	private Logger log;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public StampBrowseDialog(Shell parent, Injector injector) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setText("Stamp Selection");
		if (injector!=null) injector.injectMembers(this);
	}
	
	@Inject
	public void inject(StampRepo sr, Logger log) {
		repo = sr;
		this.log = log;
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
	}

}
