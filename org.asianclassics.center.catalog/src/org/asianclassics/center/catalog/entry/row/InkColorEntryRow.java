package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.ComboBoxEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;

public class InkColorEntryRow extends ComboBoxEntryCell {

	public InkColorEntryRow(Composite parent) {
		super(parent, "Color of Ink");
		addHorizSep();
		combo.add("Black");
		combo.add("Red");
		combo.add("Silver");
		combo.add("Gold");
		combo.add("3 colors");
		combo.add("5 colors");
		combo.add("7 colors");
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().inkColor;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().inkColor = data;
	}

}
