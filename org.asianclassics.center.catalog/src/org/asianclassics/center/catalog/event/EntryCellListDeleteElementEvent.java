package org.asianclassics.center.catalog.event;

import org.asianclassics.center.catalog.entry.cell.ModelHoldingEntryCell;

public class EntryCellListDeleteElementEvent {
	private ModelHoldingEntryCell cell;

	public EntryCellListDeleteElementEvent(ModelHoldingEntryCell cell) {
		this.cell = cell;
	}
	
	public ModelHoldingEntryCell getCell() {
		return cell;
	}

	public void setCell(ModelHoldingEntryCell cell) {
		this.cell = cell;
	}
	

}
