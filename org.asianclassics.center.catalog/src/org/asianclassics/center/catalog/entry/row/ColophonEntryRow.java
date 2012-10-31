package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;


public class ColophonEntryRow extends TextEntryCell {

	public ColophonEntryRow(Composite parent) {
		super(parent, "Colophon", BoxType.WIDE);
		addHorizSep();
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().colophon;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().colophon = data;
	}
}
