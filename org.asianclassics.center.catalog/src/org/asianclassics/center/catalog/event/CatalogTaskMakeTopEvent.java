package org.asianclassics.center.catalog.event;


public class CatalogTaskMakeTopEvent {
	public enum CatalogTaskViewType {
		SELECTION, ENTRY, TEST
	}
	private CatalogTaskViewType viewType;
	
	public CatalogTaskMakeTopEvent(CatalogTaskViewType viewType) {
		this.viewType = viewType;
	}

	public CatalogTaskViewType getViewType() {
		return viewType;
	}

	public void setViewType(CatalogTaskViewType viewType) {
		this.viewType = viewType;
	}
	

}
