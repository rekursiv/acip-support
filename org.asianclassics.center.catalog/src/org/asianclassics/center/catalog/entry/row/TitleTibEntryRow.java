package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;


public class TitleTibEntryRow extends TextEntryCell {

	public TitleTibEntryRow() {
		super("Tibetan Title");
	}
	
	@Override
	protected String getModelData() {
		return ctlr.getModel().titleTibetan;
	}
	
	@Override
	protected void setModelData(String data) {
		ctlr.getModel().titleTibetan = data;
	}
}
