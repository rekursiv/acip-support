package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;


public class LibraryNumEntryRow extends TextEntryCell {

	public LibraryNumEntryRow(Composite parent) {
		super(parent, "Library #");
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Subscribe
	public void onPreWrite(EntryModelPreWriteEvent evt) {
		onViewToModel();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().libraryNumber;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().libraryNumber = data;
	}
}
