package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.entry.row.DrawingEntryRow;
import org.asianclassics.center.catalog.entry.row.ExtraLangEntryRow;
import org.asianclassics.center.catalog.entry.row.InfoEntryRow;
import org.asianclassics.center.catalog.entry.row.LibraryNumEntryRow;
import org.asianclassics.center.catalog.entry.row.MissingPageEntryRow;
import org.asianclassics.center.catalog.entry.row.StampEntryRow;
import org.asianclassics.center.catalog.entry.row.SutraPageEntryRow;
import org.asianclassics.center.catalog.entry.row.TitleSktEntryRow;
import org.asianclassics.center.catalog.entry.row.TitleTibEntryRow;
import org.asianclassics.center.catalog.event.EntryEditEvent;
import org.asianclassics.center.catalog.event.TestEvent;
import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class EntryView extends Composite {

	private EventBus eb;
	private Button btnSubmit;
	private Injector injector;
	private EntryController ctlr;
	private Button btnSaveAsDraft;
	private Button btnDelete;
	
	private static EntryView instance;

	public EntryView(Composite parent, int style, Injector injector) {
		super(parent, SWT.NONE);
		instance = this;
		
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
		
		Button btnTest = new Button(this, SWT.NONE);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				eb.post(new TestEvent());
			}
		});
		btnTest.setText("Test");

		

		new LibraryNumEntryRow(this);  ////////////////

		
		////////////////////////////
/*
		new InfoEntryRow(this);
		new LibraryNumEntryRow(this);
		new StampEntryRow(this);
		new TitleTibEntryRow(this);
		new TitleSktEntryRow(this);
		new ExtraLangEntryRow(this);

		/////
		
		new SutraPageEntryRow(this);
	*/	
		new MissingPageEntryRow(this);
		new DrawingEntryRow(this);
		
		////////////////////////////

		

		
		if (injector!=null) injector.injectMembers(this);
		
	}
	
	@Inject
	public void inject(EventBus eb, EntryController ctlr) {
		this.eb = eb;
		this.ctlr = ctlr;
		eb.post(new EntryEditEvent(null));   ////////////////////   TEST 
	}
	
	@Subscribe
	public void onAdaptSize(ParentAdaptSizeEvent evt) {
		pack();
	}

	public static EntryView getInstance() {
		return instance;
	}
	
	public Injector getInjector() {
		return injector;
	}
	
	
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
