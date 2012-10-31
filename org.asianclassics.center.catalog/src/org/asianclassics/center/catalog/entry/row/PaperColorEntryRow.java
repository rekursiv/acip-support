package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.ComboBoxEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;

public class PaperColorEntryRow extends ComboBoxEntryCell {

	public PaperColorEntryRow(Composite parent) {
		super(parent, "Color of Paper");
		addHorizSep();
		combo.add("White paper, White edge");
		combo.add("White paper, Yellow edge");
		combo.add("White paper, Red edge");
		combo.add("White paper, Brown edge");
		combo.add("White paper, Black edge");
		combo.add("Black paper, Black edge");
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().paperColor;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().paperColor = data;
	}

}
