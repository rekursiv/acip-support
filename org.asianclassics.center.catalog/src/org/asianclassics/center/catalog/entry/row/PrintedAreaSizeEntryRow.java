package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.EntryCell;
import org.asianclassics.center.catalog.entry.cell.SizeEntryCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class PrintedAreaSizeEntryRow extends EntryCell {


	public PrintedAreaSizeEntryRow(Composite parent) {
		super(parent, "Printed Area Size");
		addHorizSep();
		
		SizeEntryCell width = new SizeEntryCell(this, "Width", 50, 100) {
			@Override
			protected void getModelData() {
				number = ctlr.getModel().getPrintedAreaWidth();
			}
			@Override
			protected void setModelData() {
				ctlr.getModel().setPrintedAreaWidth(number);
			}
		};
		FormData fd_width = new FormData();
		fd_width.top = new FormAttachment(lblTitle, -10, SWT.TOP);
		fd_width.left = new FormAttachment(lblTitle, 0, SWT.RIGHT);
		width.setLayoutData(fd_width);

		FormData fd_lblWcm = new FormData();
		Label lblWcm = new Label(this, SWT.NONE);
		fd_lblWcm.top = new FormAttachment(width, 10, SWT.TOP);
		fd_lblWcm.left = new FormAttachment(width, 1, SWT.RIGHT);
		lblWcm.setText("cm");
		lblWcm.setLayoutData(fd_lblWcm);
		
		SizeEntryCell height = new SizeEntryCell(this, "Height", 50, 100) {
			@Override
			protected void getModelData() {
				number = ctlr.getModel().getPrintedAreaHeight();
			}
			@Override
			protected void setModelData() {
				ctlr.getModel().setPrintedAreaHeight(number);
			}
		};
		FormData fd_height = new FormData();
		fd_height.top = new FormAttachment(lblWcm, -10, SWT.TOP);
		fd_height.left = new FormAttachment(lblWcm, 15, SWT.RIGHT);
		height.setLayoutData(fd_height);
		
		FormData fd_lblHcm = new FormData();
		Label lblHcm = new Label(this, SWT.NONE);
		fd_lblHcm.top = new FormAttachment(height, 10, SWT.TOP);
		fd_lblHcm.left = new FormAttachment(height, 1, SWT.RIGHT);
		lblHcm.setText("cm");
		lblHcm.setLayoutData(fd_lblHcm);
		
	}
}
