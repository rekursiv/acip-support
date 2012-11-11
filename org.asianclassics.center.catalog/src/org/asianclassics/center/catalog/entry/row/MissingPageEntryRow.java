package org.asianclassics.center.catalog.entry.row;

import java.util.LinkedList;
import java.util.List;

import org.asianclassics.center.catalog.entry.cell.LinkedEntryCell;
import org.asianclassics.center.catalog.entry.cell.ListEntryCell;
import org.asianclassics.center.catalog.entry.cell.MissingPageEntryCell;
import org.asianclassics.center.catalog.entry.model.MissingPageModel;
import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.eclipse.swt.widgets.Composite;

public class MissingPageEntryRow extends ListEntryCell {

	public MissingPageEntryRow(Composite parent) {
		super(parent, "Missing Pages", "Add Page");
	}

	@Override
	protected void onAddNewCell() {
		if (ctlr.getModel().missingPages==null) ctlr.getModel().missingPages = new LinkedList<MissingPageModel>();
		MissingPageModel pm = new MissingPageModel();
		ctlr.getModel().missingPages.add(pm);
		addCellFromObject(pm);
		eb.post(new ParentAdaptSizeEvent());
	}
	
	@Override
	protected LinkedEntryCell createNewView(Object object) {
		return new MissingPageEntryCell(cellListPanel, object);
	}
	
	@Override
	protected void copyObjectsToView() {
		List<MissingPageModel> pages = ctlr.getModel().missingPages;
		if (pages==null) return;
		for (MissingPageModel page : pages) {
			addCellFromObject(page);
		}
	}



}
