package org.asianclassics.center.catalog.entry.row;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.asianclassics.center.catalog.entry.cell.LinkedEntryCell;
import org.asianclassics.center.catalog.entry.cell.ListEntryCell;
import org.asianclassics.center.catalog.entry.cell.StampEntryCell;
import org.eclipse.swt.widgets.Composite;

public class StampEntryRow extends ListEntryCell {

	public StampEntryRow(Composite parent) {
		super(parent, "Stamps", "Add Stamp");
	}
	
	
	@Override
	protected void createNewModelListIfNeeded() {
		if (ctlr.getModel().stamps==null) ctlr.getModel().stamps = new LinkedList<AtomicInteger>();
	}
	
	@Override
	protected Object createNewObject() {
		AtomicInteger sn = new AtomicInteger();
		ctlr.getModel().stamps.add(sn);
		return sn;
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
