package org.asianclassics.center.catalog.event;

import org.asianclassics.center.catalog.entry.cell.EntryCell;

public class EntryCellListDeleteElementEvent {
	private EntryCell cell;

	public EntryCellListDeleteElementEvent(EntryCell cell) {
		this.cell = cell;
	}
	
	public EntryCell getCell() {
		return cell;
	}

	public void setCell(EntryCell cell) {
		this.cell = cell;
	}
	

}
