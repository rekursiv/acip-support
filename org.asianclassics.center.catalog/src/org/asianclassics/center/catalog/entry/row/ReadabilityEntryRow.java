package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.ComboBoxEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;

public class ReadabilityEntryRow extends ComboBoxEntryCell {

	public ReadabilityEntryRow(Composite parent) {
		super(parent, "Readablilty", true);
		addHorizSep();
		combo.add("Good");
		combo.add("Medium");
		combo.add("Bad");
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().readability;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().readability = data;
	}

}
