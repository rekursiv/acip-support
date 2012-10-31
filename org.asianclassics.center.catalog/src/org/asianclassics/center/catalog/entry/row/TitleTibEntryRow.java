package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;


public class TitleTibEntryRow extends TextEntryCell {

	public TitleTibEntryRow(Composite parent) {
		super(parent, "Tibetan Title");
		addHorizSep();
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().titleTibetan;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().titleTibetan = data;
	}
}
