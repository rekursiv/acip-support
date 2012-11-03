package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.CatalogApp;
import org.asianclassics.center.catalog.entry.row.AuthorEntryRow;
import org.asianclassics.center.catalog.entry.row.ColophonEntryRow;
import org.asianclassics.center.catalog.entry.row.DrawingEntryRow;
import org.asianclassics.center.catalog.entry.row.ExtraLangEntryRow;
import org.asianclassics.center.catalog.entry.row.FormatEntryRow;
import org.asianclassics.center.catalog.entry.row.InfoEntryRow;
import org.asianclassics.center.catalog.entry.row.InkColorEntryRow;
import org.asianclassics.center.catalog.entry.row.LibraryNumberEntryRow;
import org.asianclassics.center.catalog.entry.row.LineCountEntryRow;
import org.asianclassics.center.catalog.entry.row.LocationEntryRow;
import org.asianclassics.center.catalog.entry.row.PageSizeEntryRow;
import org.asianclassics.center.catalog.entry.row.PrintedAreaSizeEntryRow;
import org.asianclassics.center.catalog.entry.row.VolumeEntryRow;
import org.asianclassics.center.catalog.entry.row.MissingPageEntryRow;
import org.asianclassics.center.catalog.entry.row.PaperColorEntryRow;
import org.asianclassics.center.catalog.entry.row.PaperGradeEntryRow;
import org.asianclassics.center.catalog.entry.row.PaperSourceEntryRow;
import org.asianclassics.center.catalog.entry.row.ReadabilityEntryRow;
import org.asianclassics.center.catalog.entry.row.StampEntryRow;
import org.asianclassics.center.catalog.entry.row.SutraPageEntryRow;
import org.asianclassics.center.catalog.entry.row.TitleBriefEntryRow;
import org.asianclassics.center.catalog.entry.row.TitleSktEntryRow;
import org.asianclassics.center.catalog.entry.row.TitleTibEntryRow;
import org.asianclassics.center.catalog.entry.row.YearEntryRow;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class EntryView extends Composite {

	private EventBus eb;
	private EntryController ctlr;
	private Injector injector;
	private static EntryView instance;

	public EntryView(Composite parent, int style, Injector injector) {
		super(parent, SWT.NONE);
		instance = this;
		
		this.injector = injector;
		setLayout(new GridLayout(1, false));
//*
		new InfoEntryRow(this);
		new LibraryNumberEntryRow(this);
		new StampEntryRow(this);
		new TitleTibEntryRow(this);
		new TitleSktEntryRow(this);
		new ExtraLangEntryRow(this);
		new FormatEntryRow(this);
		new TitleBriefEntryRow(this);
		new AuthorEntryRow(this);
		new YearEntryRow(this);
		new InkColorEntryRow(this);
		new PaperColorEntryRow(this);
		new PaperSourceEntryRow(this);
		new PaperGradeEntryRow(this);
		new ReadabilityEntryRow(this);
		new VolumeEntryRow(this);
		new SutraPageEntryRow(this);
		new MissingPageEntryRow(this);
		new LineCountEntryRow(this);
		new PageSizeEntryRow(this);
		new PrintedAreaSizeEntryRow(this);
		new LocationEntryRow(this);
		new DrawingEntryRow(this);
		new ColophonEntryRow(this);
//*/		
		

		
		
		if (injector!=null) injector.injectMembers(this);
		
	}
	
	@Inject
	public void inject(EventBus eb, EntryController ctlr) {
		this.eb = eb;
		this.ctlr = ctlr;
		if (CatalogApp.debugMode) eb.post(new EntryEditEvent(null));   ////////////////////   TEST 
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
