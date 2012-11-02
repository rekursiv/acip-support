package org.asianclassics.center.catalog.event;

public class EntryUserMessageEvent {
	
	private String msg;
	
	public EntryUserMessageEvent(String msg) {
		this.msg = msg;
	}

	public String getMessage() {
		return msg;
	}


}
