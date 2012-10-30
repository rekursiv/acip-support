package org.asianclassics.center.catalog.entry.cell;

import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.EntryValidateEvent;

import com.google.common.eventbus.Subscribe;


public abstract class LinkedEntryCell extends EntryCell {
	
	public LinkedEntryCell(String title) {
		super(title);
	}
	
	public LinkedEntryCell(String title, int titleWidth) {
		super(title, titleWidth);
	}
	

	
	@Subscribe
	public void onPostReadEvent(EntryModelPostReadEvent evt) {
		onPostRead();
	}
	
	@Subscribe
	public void onPreWriteEvent(EntryModelPreWriteEvent evt) {
		onPreWrite();
	}
	
	@Subscribe
	public void onValidateEvent(EntryValidateEvent evt) {
		onValidate();
	}
	
	
	
	
	protected void onPostRead() {
	}
	
	protected String getModelData() {
		return null;
	}
	
	protected void onPreWrite() {
	}	
	
	protected void setModelData(String data) {
	}
	
	protected void onValidate() {
	}	


	
}
