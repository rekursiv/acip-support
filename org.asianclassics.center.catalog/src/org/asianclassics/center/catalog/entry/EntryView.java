package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.entry.TextEntryRow.BoxType;
import org.asianclassics.center.catalog.entry.row.TitleTibEntryRow;
import org.asianclassics.center.catalog.event.TestEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class EntryView extends Composite {

	private EventBus eb;
	private Button btnTest;
	private Injector injector;
	private EntryController ctlr;

	public EntryView(Composite parent, int style, Injector injector) {
		super(parent, SWT.NONE);
		this.injector = injector;
		setLayout(new GridLayout(1, false));

		new TitleTibEntryRow(this);
		
		btnTest = new Button(this, SWT.NONE);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ctlr.test();
//				eb.post(new TestEvent());
			}
		});
		btnTest.setText("Test");

		
		
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
