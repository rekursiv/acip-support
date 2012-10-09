package org.asianclassics.center.event;

public class LoginFailureEvent extends LoginEvent {

	private String errMsg;

	public LoginFailureEvent(String workerId, String errMsg) {
		super(workerId);
		this.setErrorMessage(errMsg);
	}

	public String getErrorMessage() {
		return errMsg;
	}

	public void setErrorMessage(String errMsg) {
		this.errMsg = errMsg;
	}

}
