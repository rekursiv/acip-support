package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.TextEntryRow;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.EntryValidateEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.SWT;


public class LibraryNumEntryRow extends TextEntryCell {

	public LibraryNumEntryRow() {
		super("Library #");
	}
	
	@Override
	protected String getModelData() {
		return ctlr.getModel().libraryNumber;
	}
	
	@Override
	protected void setModelData(String data) {
		ctlr.getModel().libraryNumber = data;
	}
}
