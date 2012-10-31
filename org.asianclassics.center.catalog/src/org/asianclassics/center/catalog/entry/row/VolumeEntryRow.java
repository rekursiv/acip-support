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


public class VolumeEntryRow extends TextEntryCell {

	public VolumeEntryRow(Composite parent) {
		super(parent, "Volume", BoxType.SIMPLE);
		addHorizSep();
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().volume;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().volume = data;
	}
}
