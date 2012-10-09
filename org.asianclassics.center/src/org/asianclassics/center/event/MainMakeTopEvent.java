package org.asianclassics.center.event;

public class MainMakeTopEvent {
	
	public enum MainViewType {
		LOGIN, TASK
	}

	private MainViewType mvt;
	
	public MainMakeTopEvent(MainViewType mvt) {
		this.setMvt(mvt);
	}

	public MainViewType getMvt() {
		return mvt;
	}

	public void setMvt(MainViewType mvt) {
		this.mvt = mvt;
	}

}
