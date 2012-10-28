package org.asianclassics.center.catalog.event;

import org.asianclassics.center.catalog.entry.model.EntryModel;

public class EntryEditEvent {
	private EntryModel entry;
	
	public EntryEditEvent(EntryModel entry) {
		this.entry = entry;
	}
	
	public EntryModel getEntry() {
		return entry;
	}

}
