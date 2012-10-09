package org.asianclassics.center.event;

public class StatusPanelUpdateEvent {
	public String message;
	public String ip;
	public String nodeType;

	public StatusPanelUpdateEvent(String message) {
		this.message = message;
		nodeType = null;
		ip = null;
	}
	
	public StatusPanelUpdateEvent(String message, String ip, String nodeType) {
		this.message = message;
		this.ip = ip;
		this.nodeType = nodeType;
	}
	

}
