package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;


public class LineCountEntryRow extends TextEntryCell {

	public LineCountEntryRow(Composite parent) {
		super(parent, "Lines Per Page", BoxType.SIMPLE);
		addHorizSep();
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = String.valueOf(ctlr.getModel().linesPerPage);
	}
	
	@Override
	protected void setModelData() {
		try {
			ctlr.getModel().linesPerPage = Integer.parseInt(data);
		} catch (NumberFormatException e) {
			ctlr.getModel().linesPerPage = 0;
		}
	}
	
	@Override
	protected void onValidate() {
		int num = -1;
		try {
			num = Integer.parseInt(text.getText());			
		} catch (NumberFormatException e) {	}
		if (num<0) {
			ctlr.invalidate();
			setHilite(HiliteMode.INVALID);
		}
	}
}
