package org.asianclassics.center.input.upload;

import java.util.logging.Logger;

import org.asianclassics.center.input.dispatch.DispatchManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.google.inject.Inject;
import com.google.inject.Injector;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class UploadView extends Composite {

	
	private static final String defaultColName = "tengyur";
	private static final String defaultBaseDir = "C:/projects/ACIP/input_test_sample/";
	
	private Logger log;
	private UploadController ctlr;
	private Text txtBaseDir;
	private Text txtColName;
	private Spinner spnPgUl;
	
	private DispatchManager dm;


	public UploadView(Composite parent, Injector injector) {
		super(parent, SWT.NONE);
		
		Label lblBaseDir = new Label(this, SWT.NONE);
		lblBaseDir.setBounds(10, 10, 55, 15);
		lblBaseDir.setText("Base Dir:");
		
		txtBaseDir = new Text(this, SWT.BORDER);
		txtBaseDir.setBounds(71, 10, 594, 21);
		txtBaseDir.setText(defaultBaseDir);
		
		Label lblCollectionName = new Label(this, SWT.NONE);
		lblCollectionName.setBounds(10, 37, 103, 21);
		lblCollectionName.setText("Collection Name:");
		
		txtColName = new Text(this, SWT.BORDER);
		txtColName.setBounds(119, 37, 222, 21);
		txtColName.setText(defaultColName);
		
		Label lblPagesToUpload = new Label(this, SWT.NONE);
		lblPagesToUpload.setBounds(10, 79, 115, 15);
		lblPagesToUpload.setText("Pages To Upload:");
		
		spnPgUl = new Spinner(this, SWT.BORDER);
		spnPgUl.setBounds(131, 79, 47, 22);
		spnPgUl.setSelection(10);
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ctlr.setBaseDir(txtBaseDir.getText());
				ctlr.setColName(txtColName.getText());
				ctlr.setMaxPages(spnPgUl.getSelection());
				ctlr.upload();
			}
		});
		btnNewButton.setBounds(245, 79, 75, 25);
		btnNewButton.setText("Upload");
		
		Button btnDispatch = new Button(this, SWT.NONE);
		btnDispatch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				dm.dispatch();
			}
		});
		btnDispatch.setBounds(379, 79, 75, 25);
		btnDispatch.setText("Dispatch");
		
		Button btnResetHqDb = new Button(this, SWT.NONE);
		btnResetHqDb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ctlr.reset();
			}
		});
		btnResetHqDb.setBounds(22, 138, 103, 25);
		btnResetHqDb.setText("Reset HQ DB");
		
		Button btnResetCenterDb = new Button(this, SWT.NONE);
		btnResetCenterDb.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				dm.reset();
			}
		});
		btnResetCenterDb.setBounds(205, 138, 115, 25);
		btnResetCenterDb.setText("Reset Center DB");

		if (injector!=null) injector.injectMembers(this);
	}

	@Inject
	public void inject(Logger log, UploadController ctlr) {
		this.log = log;
		this.ctlr = ctlr;

		dm = new DispatchManager();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
