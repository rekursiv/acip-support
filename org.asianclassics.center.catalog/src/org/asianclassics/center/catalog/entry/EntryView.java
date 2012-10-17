package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.entry.TextEntryRow.BoxType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class EntryView extends Composite {


	public EntryView(Composite parent, int style) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));

		
		new TextEntryRow(this, "Standard", null, BoxType.STANDARD);
		new ComboBoxEntryRow(this, "Any Color", null);
		new TextEntryRow(this, "Simple", null, BoxType.SIMPLE);
		new ComboBoxEntryRow(this, "Colors", null, true);
		new TextEntryRow(this, "Wide", null, BoxType.WIDE);


	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
