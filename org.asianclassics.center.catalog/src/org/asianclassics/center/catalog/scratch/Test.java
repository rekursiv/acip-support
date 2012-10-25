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


public class Test extends Composite {
	private Combo combo;
	private Button btnRead;
	private Button btnDebug;
	private EventBus eb;
	private LinkManager lm;
	private Group grpFinishedSutras;
	private Group grpSutrasToBe;
	private List list;


	public Test(Composite parent, int style, Injector injector) {
		super(parent, style);
		
		grpFinishedSutras = new Group(this, SWT.NONE);
		grpFinishedSutras.setText("Edit a sutra you have worked on before");
		grpFinishedSutras.setBounds(35, 207, 279, 199);
		
		list = new List(grpFinishedSutras, SWT.BORDER | SWT.V_SCROLL);
		list.setBounds(41, 45, 102, 114);
		list.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		list.setItems(new String[] {"M00432-001", "M00432-002", "M00432-003", "M00432-004", "M00432-005", "M00433-001", "M00433-002"});
		
		grpSutrasToBe = new Group(this, SWT.NONE);
		grpSutrasToBe.setText("Enter a new sutra");
		grpSutrasToBe.setBounds(37, 92, 174, 97);
		

		

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
