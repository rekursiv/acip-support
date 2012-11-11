package org.asianclassics.center.catalog.entry.row;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.asianclassics.center.catalog.entry.EntryView;
import org.asianclassics.center.catalog.entry.StampSelectDialog;
import org.asianclassics.center.catalog.entry.cell.LinkedEntryCell;
import org.asianclassics.center.catalog.entry.cell.ListEntryCell;
import org.asianclassics.center.catalog.entry.cell.StampEntryCell;
import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.eclipse.swt.widgets.Composite;

public class StampEntryRow extends ListEntryCell {

	private StampSelectDialog stampSelDlg;

	public StampEntryRow(Composite parent) {
		super(parent, "Stamps", "Add Stamp");
		stampSelDlg = new StampSelectDialog(getShell(), EntryView.getInstance().getInjector());
	}
	
	
	@Override
	protected void onAddNewCell() {
		int result = stampSelDlg.open();
		if (result>0) {
			if (ctlr.getModel().stamps==null) ctlr.getModel().stamps = new LinkedList<AtomicInteger>();
			AtomicInteger sn = new AtomicInteger(result);
			ctlr.getModel().stamps.add(sn);
			addCellFromObject(sn);
			eb.post(new ParentAdaptSizeEvent());
		}
	}
	
	@Override
	protected LinkedEntryCell createNewView(Object object) {
		return new StampEntryCell(cellListPanel, object);
	}
	
	@Override
	protected void copyObjectsToView() {
		List<AtomicInteger> stamps = ctlr.getModel().stamps;
		if (stamps==null) return;
		for (AtomicInteger stampNum : stamps) {
			addCellFromObject(stampNum);
		}
	}
	
	
}
