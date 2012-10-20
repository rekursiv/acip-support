package org.asianclassics.center.catalog.entry.model;


public class DrawingModel {
	private String page;
	private String position;
	private boolean hasColors;
	private String caption;
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public boolean isHasColors() {
		return hasColors;
	}
	public void setHasColors(boolean hasColors) {
		this.hasColors = hasColors;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
}
