package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.EntryCell;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell.BoxType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;

public class SutraPageEntryRow extends EntryCell {


	public SutraPageEntryRow(Composite parent) {
		super(parent, "Sutra Pages");

		TextEntryCell begin = new TextEntryCell(this, "Begin", 50, BoxType.SIMPLE, 100) {
			@Override
			protected void getModelData() {
				data = ctlr.getModel().sutraPages.begin;
			}
			
			@Override
			protected void setModelData() {
				ctlr.getModel().sutraPages.begin = data;
			}
		};
		FormData fd_begin = new FormData();
		fd_begin.top = new FormAttachment(lblTitle, -10, SWT.TOP);
		fd_begin.left = new FormAttachment(lblTitle, 0, SWT.RIGHT);
		begin.setLayoutData(fd_begin);

		
		TextEntryCell end = new TextEntryCell(this, "End", 40, BoxType.SIMPLE, 100) {
			@Override
			protected void getModelData() {
				data = ctlr.getModel().sutraPages.end;
			}
			
			@Override
			protected void setModelData() {
				ctlr.getModel().sutraPages.end = data;
			}
		};
		FormData fd_end = new FormData();
		fd_end.top = new FormAttachment(begin, 0, SWT.TOP);
		fd_end.left = new FormAttachment(begin, 0, SWT.RIGHT);
		end.setLayoutData(fd_end);

	}
}
