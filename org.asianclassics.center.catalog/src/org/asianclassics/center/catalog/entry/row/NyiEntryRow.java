package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.EntryRow;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class NyiEntryRow extends EntryRow {
	private Label lblNotYet;

	public NyiEntryRow(Composite parent, String rowLabel) {
		super(parent, rowLabel);
		
		lblNotYet = new Label(this, SWT.NONE);
		FormData fd_lblNotYet = new FormData();
		fd_lblNotYet.top = new FormAttachment(lblRow, 0, SWT.TOP);
		fd_lblNotYet.left = new FormAttachment(0, 115);
		lblNotYet.setLayoutData(fd_lblNotYet);
		lblNotYet.setText("( Not Yet Implemented )");
	}
	
	

}
