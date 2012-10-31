package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.ComboBoxEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;

public class PaperGradeEntryRow extends ComboBoxEntryCell {

	public PaperGradeEntryRow(Composite parent) {
		super(parent, "Grade of Paper", true);
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
		data = ctlr.getModel().paperGrade;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().paperGrade = data;
	}

}
