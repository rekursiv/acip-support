package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.EntryCell;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell.BoxType;
import org.asianclassics.center.catalog.entry.model.SizeModel;
import org.asianclassics.center.catalog.entry.model.SutraPageModel;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.google.common.eventbus.Subscribe;

public class PrintedAreaSizeEntryRow extends EntryCell {


	public PrintedAreaSizeEntryRow(Composite parent) {
		super(parent, "Printed Area Size");
		addHorizSep();

		TextEntryCell width = new TextEntryCell(this, "Width", 40, BoxType.SIMPLE, 100) {
			
			@Subscribe
			public void onPostRead(EntryModelPostReadEvent evt) {
				onModelToView();
			}
			
			@Override
			protected void getModelData() {
				if (ctlr.getModel().printedAreaSize==null) data = null;   // FIXME:  this should be done in the model
				else data = String.valueOf(ctlr.getModel().printedAreaSize.width);
			}
			
			@Override
			protected void setModelData() {
				if (ctlr.getModel().printedAreaSize==null) ctlr.getModel().printedAreaSize = new SizeModel();  // FIXME:  this should be done in the model
				ctlr.getModel().printedAreaSize.width = Integer.parseInt(data);
			}
		};
		FormData fd_width = new FormData();
		fd_width.top = new FormAttachment(lblTitle, -10, SWT.TOP);
		fd_width.left = new FormAttachment(lblTitle, 0, SWT.RIGHT);
		width.setLayoutData(fd_width);

		FormData fd_lblWmm = new FormData();
		Label lblWmm = new Label(this, SWT.NONE);
		fd_lblWmm.top = new FormAttachment(width, 10, SWT.TOP);
		fd_lblWmm.left = new FormAttachment(width, 1, SWT.RIGHT);
		lblWmm.setText("mm");
		lblWmm.setLayoutData(fd_lblWmm);
		
		TextEntryCell height = new TextEntryCell(this, "Height", 40, BoxType.SIMPLE, 100) {
			
			@Subscribe
			public void onPostRead(EntryModelPostReadEvent evt) {
				onModelToView();
			}
			
			@Override
			protected void getModelData() {
				if (ctlr.getModel().printedAreaSize==null) data = null;   // FIXME:  this should be done in the model
				else data = String.valueOf(ctlr.getModel().printedAreaSize.height);
			}
			
			@Override
			protected void setModelData() {
				if (ctlr.getModel().printedAreaSize==null) ctlr.getModel().printedAreaSize = new SizeModel();  // FIXME:  this should be done in the model
				ctlr.getModel().printedAreaSize.height = Integer.parseInt(data);
			}
		};
		FormData fd_height = new FormData();
		fd_height.top = new FormAttachment(lblWmm, -10, SWT.TOP);
		fd_height.left = new FormAttachment(lblWmm, 15, SWT.RIGHT);
		height.setLayoutData(fd_height);

		
		FormData fd_lblHmm = new FormData();
		Label lblHmm = new Label(this, SWT.NONE);
		fd_lblHmm.top = new FormAttachment(height, 10, SWT.TOP);
		fd_lblHmm.left = new FormAttachment(height, 1, SWT.RIGHT);
		lblHmm.setText("mm");
		lblHmm.setLayoutData(fd_lblHmm);
	}
}
