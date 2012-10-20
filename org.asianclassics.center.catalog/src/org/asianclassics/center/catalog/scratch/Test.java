package org.asianclassics.center.catalog.scratch;


import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.center.link.LinkManager;
import org.asianclassics.database.CustomCouchDbConnector;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Injector;


public class Test extends Composite {
	private Combo combo;
	private Button btnRead;
	private Button btnDebug;
	private EventBus eb;
	private LinkManager lm;


	public Test(Composite parent, int style, Injector injector) {
		super(parent, style);
		
		btnRead = new Button(this, SWT.NONE);
		btnRead.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				read();
			}


		});
		btnRead.setBounds(10, 10, 75, 25);
		btnRead.setText("Read");
		
		btnDebug = new Button(this, SWT.NONE);
		btnDebug.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				debug();
			}
		});
		btnDebug.setBounds(10, 54, 75, 25);
		btnDebug.setText("Debug");
		

		if (injector!=null) injector.injectMembers(this);
	}
	
	@Inject
	public void inject(EventBus eb, LinkManager lm) {
		this.eb = eb;
		this.lm = lm;
	}

	private void read() {
		CouchDbInstance couch = lm.getServerLink();
		CouchDbConnector db = new CustomCouchDbConnector("acip-nlm-catalog", couch);
//		db.
		EntryModel entry = db.get(EntryModel.class, "M0057413-001");
//		entry.test();
	}
	
	private void debug() {
		
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
