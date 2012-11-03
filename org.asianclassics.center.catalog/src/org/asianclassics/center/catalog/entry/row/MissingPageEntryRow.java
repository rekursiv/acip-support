package org.asianclassics.center.catalog.entry.row;

import java.util.LinkedList;
import java.util.List;

import org.asianclassics.center.catalog.entry.cell.ListEntryCell;
import org.asianclassics.center.catalog.entry.cell.MissingPageEntryCell;
import org.asianclassics.center.catalog.entry.cell.ModelHoldingEntryCell;
import org.asianclassics.center.catalog.entry.model.MissingPageModel;
import org.asianclassics.center.catalog.entry.model.Model;
import org.eclipse.swt.widgets.Composite;

public class MissingPageEntryRow extends ListEntryCell {

	public MissingPageEntryRow(Composite parent) {
		super(parent, "Missing Pages", "Add Page");
	}

	@Override
	protected void createNewModelListIfNeeded() {
		if (ctlr.getModel().missingPages==null) ctlr.getModel().missingPages = new LinkedList<MissingPageModel>();
	}
	
	@Override
	protected Model createNewModel() {
		MissingPageModel pm = new MissingPageModel();
		ctlr.getModel().missingPages.add(pm);
		return pm;
	}
	
	@Override
	protected ModelHoldingEntryCell createNewView(Model model) {
		return new MissingPageEntryCell(cellListPanel, model);
	}
	
	@Override
	protected void copyModelToView() {
		List<MissingPageModel> pages = ctlr.getModel().missingPages;
		if (pages==null) return;
		for (MissingPageModel page : pages) {
			addCellFromModel(page);
		}
	}



}
