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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;


public class Test extends Composite {
	private Combo combo;
	private Button btnRead;
	private Button btnDebug;
	private EventBus eb;
	private LinkManager lm;
	private Group grpSutrasToBe;
	private Button btnAddSutra;
	private Button btnBeginANew;
	private Table table;
	private TableViewer tableViewer;
	private TableColumn tblclmnFoo;
	private TableViewerColumn tableViewerColumn;
	private TableColumn tblclmnBar;
	private TableViewerColumn tableViewerColumn_1;


	public Test(Composite parent, int style, Injector injector) {
		super(parent, style);
		
		grpSutrasToBe = new Group(this, SWT.NONE);
		grpSutrasToBe.setText("Enter a new sutra");
		grpSutrasToBe.setBounds(10, 10, 279, 97);
		
		btnAddSutra = new Button(grpSutrasToBe, SWT.NONE);
		btnAddSutra.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnAddSutra.setBounds(10, 24, 203, 25);
		btnAddSutra.setText("Add sutra 4 to poti 57422");
		
		btnBeginANew = new Button(grpSutrasToBe, SWT.NONE);
		btnBeginANew.setBounds(10, 62, 203, 25);
		btnBeginANew.setText("Begin new poti");
		
		tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setBounds(31, 151, 306, 181);
		
		tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		tblclmnFoo = tableViewerColumn.getColumn();
		tblclmnFoo.setWidth(120);
		tblclmnFoo.setText("Foo");
		
		tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		tblclmnBar = tableViewerColumn_1.getColumn();
		tblclmnBar.setWidth(100);
		tblclmnBar.setText("Bar");
		

		

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
