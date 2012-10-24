package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.entry.row.TitleSktEntryRow;
import org.asianclassics.center.catalog.entry.row.TitleTibEntryRow;
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
	private Button btnRead;
	private Injector injector;
	private EntryController ctlr;
	private Button btnWrite;
	private Button btnValidate;

	public EntryView(Composite parent, int style, Injector injector) {
		super(parent, SWT.NONE);
		this.injector = injector;
		setLayout(new GridLayout(1, false));

		new TitleTibEntryRow(this);
		new TitleSktEntryRow(this);

		
		
		
		
		
		btnRead = new Button(this, SWT.NONE);
		btnRead.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ctlr.read();
			}
		});
		btnRead.setText("Read");
		
		btnWrite = new Button(this, SWT.NONE);
		btnWrite.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ctlr.write();
			}
		});
		btnWrite.setText("Write");
		
		btnValidate = new Button(this, SWT.NONE);
		btnValidate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ctlr.validate();
			}
		});
		btnValidate.setText("Validate");

		
		
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
	}

	public Injector getInjector() {
		return injector;
	}
	
	
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
