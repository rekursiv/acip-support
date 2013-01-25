package org.asianclassics.center.catalog.scratch;

import java.util.logging.Logger;

import org.asianclassics.center.catalog.entry.model.StampRepo;
import org.asianclassics.center.catalog.entry.stamp.StampBrowseDialog;
import org.asianclassics.center.catalog.entry.stamp.StampScanDialog;
import org.asianclassics.center.catalog.entry.stamp.StampSelectDialog;
import org.eclipse.swt.widgets.Composite;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class TestPanel extends Composite {

	private StampRepo repo;
	private Logger log;
	private Button btnOpenDialog;
	
	private StampSelectDialog dlg;
//	private StampScanDialog dlg;


	public TestPanel(Composite parent, int style, Injector injector) {
		super(parent, style);
		
		dlg = new StampSelectDialog(getShell(), injector);
//		dlg = new StampScanDialog(getShell(), injector);
		
		btnOpenDialog = new Button(this, SWT.NONE);
		btnOpenDialog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				openDialog();
			}
		});
		btnOpenDialog.setBounds(10, 10, 75, 25);
		btnOpenDialog.setText("Open Dialog");
		
		if (injector!=null) injector.injectMembers(this);
		
	}
	
	@Inject
	public void inject(StampRepo sr, Logger log) {
		repo = sr;
		this.log = log;
	}

	private void openDialog() {
		int r = dlg.open();
		log.info("dlg returned: "+r);
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
