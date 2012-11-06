package org.asianclassics.center.event;

public class StatusPanelUpdateEvent {
	private String message;
	private String ip;
	private String nodeType;

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

	public String getMessage() {
		return message;
	}

	public String getIp() {
		return ip;
	}

	public String getNodeType() {
		return nodeType;
	}
	

}
