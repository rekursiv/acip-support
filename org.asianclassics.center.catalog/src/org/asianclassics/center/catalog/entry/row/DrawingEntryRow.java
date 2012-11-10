package org.asianclassics.center.catalog.entry.row;

import java.util.LinkedList;
import java.util.List;

import org.asianclassics.center.catalog.entry.cell.DrawingEntryCell;
import org.asianclassics.center.catalog.entry.cell.LinkedEntryCell;
import org.asianclassics.center.catalog.entry.cell.ListEntryCell;
import org.asianclassics.center.catalog.entry.model.DrawingModel;
import org.eclipse.swt.widgets.Composite;

public class DrawingEntryRow extends ListEntryCell {

	public DrawingEntryRow(Composite parent) {
		super(parent, "Drawings", "Add Drawing");
	}

	@Override
	protected void createNewModelListIfNeeded() {
		if (ctlr.getModel().drawings==null) ctlr.getModel().drawings = new LinkedList<DrawingModel>();
	}
	
	@Override
	protected Object createNewObject() {
		DrawingModel model = new DrawingModel();
		ctlr.getModel().drawings.add(model);
		return model;
	}
	
	@Override
	protected LinkedEntryCell createNewView(Object object) {
		return new DrawingEntryCell(cellListPanel, object);
	}
	
	@Override
	protected void copyObjectsToView() {
		List<DrawingModel> drawings = ctlr.getModel().drawings;
		if (drawings==null) return;
		for (DrawingModel drawing : drawings) {
			addCellFromObject(drawing);
		}
	}

}
