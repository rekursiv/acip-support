package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.EntryCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class NyiEntryRow extends EntryCell {
	private Label lblNotYet;
	
	
	public NyiEntryRow(Composite parent, String rowLabel) {
		super(parent, rowLabel);
		
		lblNotYet = new Label(this, SWT.NONE);
		FormData fd_lblNotYet = new FormData();
		fd_lblNotYet.top = new FormAttachment(lblTitle, 0, SWT.TOP);
		fd_lblNotYet.left = new FormAttachment(lblTitle, 20);
		lblNotYet.setLayoutData(fd_lblNotYet);
		lblNotYet.setText("( Not Yet Implemented )");
	}
	

}
