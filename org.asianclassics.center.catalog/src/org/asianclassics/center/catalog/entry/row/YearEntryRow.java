package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;


public class YearEntryRow extends TextEntryCell {

	public YearEntryRow(Composite parent) {
		super(parent, "Year");
		addHorizSep();
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().originDate;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().originDate = data;
	}
}
