package org.asianclassics.center.event;

public class LoginMessageEvent {

	private String msg;

	public LoginMessageEvent(String msg) {
		this.msg = msg;
	}

	public String getMessage() {
		return msg;
	}


}
