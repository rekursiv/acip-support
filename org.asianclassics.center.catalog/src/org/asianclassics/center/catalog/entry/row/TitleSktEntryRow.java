package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;


public class TitleSktEntryRow extends TextEntryCell {

	public TitleSktEntryRow() {
		super("Sanskrit Title");
	}
	
	@Override
	protected String getModelData() {
		return ctlr.getModel().titleSanskrit;
	}
	
	@Override
	protected void setModelData(String data) {
		ctlr.getModel().titleSanskrit = data;
	}
}
