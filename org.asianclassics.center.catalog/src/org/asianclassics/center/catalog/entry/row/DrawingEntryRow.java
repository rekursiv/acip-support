package org.asianclassics.center.catalog.entry.row;

import java.util.LinkedList;
import java.util.List;

import org.asianclassics.center.catalog.entry.cell.DrawingEntryCell;
import org.asianclassics.center.catalog.entry.cell.ListEntryCell;
import org.asianclassics.center.catalog.entry.cell.ModelHoldingEntryCell;
import org.asianclassics.center.catalog.entry.model.DrawingModel;
import org.asianclassics.center.catalog.entry.model.Model;
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
	protected Model createNewModel() {
		DrawingModel model = new DrawingModel();
		ctlr.getModel().drawings.add(model);
		return model;
	}
	
	@Override
	protected ModelHoldingEntryCell createNewView(Model model) {
		return new DrawingEntryCell(cellListPanel, model);
	}
	
	@Override
	protected void copyModelToView() {
		List<DrawingModel> drawings = ctlr.getModel().drawings;
		if (drawings==null) return;
		for (DrawingModel drawing : drawings) {
			addCellFromModel(drawing);
		}
	}

}
