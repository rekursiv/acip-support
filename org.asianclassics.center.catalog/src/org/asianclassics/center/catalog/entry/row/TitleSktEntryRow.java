package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.eclipse.swt.widgets.Composite;


public class TitleSktEntryRow extends TextEntryCell {

	public TitleSktEntryRow(Composite parent) {
		super(parent, "Sanskrit Title");
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().titleSanskrit;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().titleSanskrit = data;
	}
}
