package org.asianclassics.center.event;

public class LoginEvent {
	
	private String workerId;
	
	public LoginEvent(String workerId) {
		this.setWorkerId(workerId);
	}

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}


}
