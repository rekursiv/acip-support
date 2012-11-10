package org.asianclassics.center.catalog.event;

import org.asianclassics.center.catalog.entry.cell.LinkedEntryCell;

public class EntryCellListDeleteElementEvent {
	private LinkedEntryCell cell;

	public EntryCellListDeleteElementEvent(LinkedEntryCell cell) {
		this.cell = cell;
	}
	
	public LinkedEntryCell getCell() {
		return cell;
	}

	public void setCell(LinkedEntryCell cell) {
		this.cell = cell;
	}
	

}
