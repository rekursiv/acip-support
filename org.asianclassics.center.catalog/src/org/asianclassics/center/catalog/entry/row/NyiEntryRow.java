package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.EntryCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class NyiEntryRow extends EntryCell {
	private Label lblNotYet;
	
	/**
	 * @wbp.parser.constructor
	 */
	public NyiEntryRow(Composite parent) {
		super(parent);
	}
	
	public NyiEntryRow(String rowLabel) {
		super(rowLabel);
	}

	@Override
	protected void buildGui() {
		super.buildGui();
		
		lblNotYet = new Label(this, SWT.NONE);
		FormData fd_lblNotYet = new FormData();
		fd_lblNotYet.top = new FormAttachment(lblTitle, 0, SWT.TOP);
		fd_lblNotYet.left = new FormAttachment(0, 115);
		lblNotYet.setLayoutData(fd_lblNotYet);
		lblNotYet.setText("( Not Yet Implemented )");
	}
	

}
