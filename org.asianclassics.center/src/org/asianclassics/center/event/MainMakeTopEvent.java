package org.asianclassics.center.event;

public class MainMakeTopEvent {
	
	public enum MainViewType {
		LOGIN, TASK
	}

	private MainViewType viewType;
	
	public MainMakeTopEvent(MainViewType mvt) {
		this.setViewType(mvt);
	}

	public MainViewType getViewType() {
		return viewType;
	}

	public void setViewType(MainViewType mvt) {
		this.viewType = mvt;
	}

}
