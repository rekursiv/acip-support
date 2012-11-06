package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.ComboBoxEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;

public class PaperSourceEntryRow extends ComboBoxEntryCell {

	public PaperSourceEntryRow(Composite parent) {
		super(parent, "Source of Paper");
		addHorizSep();
		combo.add("Tibet");
		combo.add("Mongolia");
		combo.add("China");
		combo.add("Russia");
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().paperSource;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().paperSource = data;
	}

}
