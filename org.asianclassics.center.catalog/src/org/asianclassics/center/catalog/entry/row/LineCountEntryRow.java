package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell.BoxType;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.google.common.eventbus.Subscribe;


public class LineCountEntryRow extends TextEntryCell {

	public LineCountEntryRow(Composite parent) {
		super(parent, "Lines Per Page", BoxType.SIMPLE);
		addHorizSep();
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = String.valueOf(ctlr.getModel().linesPerPage);
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().linesPerPage = Integer.parseInt(data);
	}
}
