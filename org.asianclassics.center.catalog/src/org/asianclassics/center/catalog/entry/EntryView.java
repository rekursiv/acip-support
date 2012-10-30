package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.entry.row.ExtraLangEntryRow;
import org.asianclassics.center.catalog.entry.row.InfoEntryRow;
import org.asianclassics.center.catalog.entry.row.LibraryNumEntryRow;
import org.asianclassics.center.catalog.entry.row.StampsEntryRow;
import org.asianclassics.center.catalog.entry.row.TitleSktEntryRow;
import org.asianclassics.center.catalog.entry.row.TitleTibEntryRow;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.EntryEditEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.swt.widgets.Label;

public class EntryView extends Composite {

	private EventBus eb;
	private Button btnSubmit;
	private Injector injector;
	private EntryController ctlr;
	private Button btnSaveAsDraft;
	private Button btnDelete;

	public EntryView(Composite parent, int style, Injector injector) {
		super(parent, SWT.NONE);
		this.injector = injector;
		setLayout(new GridLayout(1, false));

		btnSubmit = new Button(this, SWT.NONE);
		btnSubmit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ctlr.submit();
			}
		});
		btnSubmit.setText("Submit");
		
		btnSaveAsDraft = new Button(this, SWT.NONE);
		btnSaveAsDraft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ctlr.saveAsDraft();
			}
		});
		btnSaveAsDraft.setText("Save as draft");
		
		btnDelete = new Button(this, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ctlr.test();
			}
		});
		btnDelete.setText("Delete");

		
		
		
		////////////////////////////

		new InfoEntryRow(this);
		new LibraryNumEntryRow(this);
		new StampsEntryRow(this);
		new TitleTibEntryRow(this);
		new TitleSktEntryRow(this);
		new ExtraLangEntryRow(this);
		
		
		////////////////////////////
		
		
/*		
		
		new TextEntryRow(this, "Standard", null, BoxType.STANDARD);
		new ComboBoxEntryRow(this, "Any Color", null);
		new TextEntryRow(this, "Simple", null, BoxType.SIMPLE);
		new ComboBoxEntryRow(this, "Colors", null, true);
		new TextEntryRow(this, "Wide", null, BoxType.WIDE);
*/

		
		if (injector!=null) injector.injectMembers(this);
		
	}
	
	@Inject
	public void inject(EventBus eb, EntryController ctlr) {
		this.eb = eb;
		this.ctlr = ctlr;
		eb.post(new EntryEditEvent(null));   ////////////////////   TEST 
	}

	public Injector getInjector() {
		return injector;
	}
	
	
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
