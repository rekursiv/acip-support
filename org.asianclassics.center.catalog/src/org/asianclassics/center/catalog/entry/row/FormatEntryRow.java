package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.ComboBoxEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;

public class FormatEntryRow extends ComboBoxEntryCell {

	public FormatEntryRow(Composite parent) {
		super(parent, "Format", true);
		addHorizSep();
		combo.add("Xylograph");
		combo.add("Typography");
		combo.add("Manuscript");
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().format;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().format = data;
	}

}
