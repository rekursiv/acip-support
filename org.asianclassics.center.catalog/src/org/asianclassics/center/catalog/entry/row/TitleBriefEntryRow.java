package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;


public class TitleBriefEntryRow extends TextEntryCell {

	public TitleBriefEntryRow(Composite parent) {
		super(parent, "Brief Title");
		addHorizSep();
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().titleTibetanBrief;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().titleTibetanBrief = data;
	}
}
