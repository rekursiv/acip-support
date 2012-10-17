package org.asianclassics.center.catalog.entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class ComboBoxEntryRow extends EntryRow {
	protected static final int boxWidth = 300;
	private Combo combo;


	public ComboBoxEntryRow(final Composite parent, String rowLabel, String dbField) {
		this(parent, rowLabel, dbField, false);
	}
	
	public ComboBoxEntryRow(final Composite parent, String rowLabel, String dbField, boolean isReadOnly) {
		super(parent, rowLabel, dbField);
		
		combo = new Combo(this, isReadOnly?SWT.READ_ONLY:SWT.NONE);
		FormData fd_combo = new FormData();
		fd_combo.right = new FormAttachment(0, boxWidth);
		fd_combo.top = new FormAttachment(lblRow, 0, SWT.TOP);
		fd_combo.left = new FormAttachment(lblRow, 6);
		combo.setLayoutData(fd_combo);

		
		//////
		
		combo.add("Red");
		combo.add("Green");
		combo.add("Blue");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

