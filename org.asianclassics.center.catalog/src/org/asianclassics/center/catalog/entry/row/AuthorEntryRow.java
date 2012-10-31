package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;


public class AuthorEntryRow extends TextEntryCell {

	public AuthorEntryRow(Composite parent) {
		super(parent, "Author");
		addHorizSep();
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().author;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().author = data;
	}
}
