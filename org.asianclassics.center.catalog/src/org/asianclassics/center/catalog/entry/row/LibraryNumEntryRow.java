package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.eclipse.swt.widgets.Composite;


public class LibraryNumEntryRow extends TextEntryCell {

	public LibraryNumEntryRow(Composite parent) {
		super(parent, "Library #");
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
