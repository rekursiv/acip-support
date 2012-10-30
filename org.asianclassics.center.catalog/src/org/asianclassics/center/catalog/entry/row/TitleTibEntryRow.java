package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.eclipse.swt.widgets.Composite;


public class TitleTibEntryRow extends TextEntryCell {

	public TitleTibEntryRow(Composite parent) {
		super(parent, "Tibetan Title");
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
