package org.asianclassics.center.catalog.entry.cell;

import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.EntryValidateEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;


public abstract class LinkedEntryCell extends EntryCell {
	
	public enum HiliteMode {
		NONE, COPIED, INVALID
	}
	
	public LinkedEntryCell(Composite parent, String title) {
		super(parent, title);
	}
	
	public LinkedEntryCell(Composite parent, String title, int titleWidth) {
		super(parent, title, titleWidth);
	}
	

	
	@Subscribe
	public void onPostReadEvent(EntryModelPostReadEvent evt) {
		getModelData();
		formatDataForGui();
		setGuiData();
	}
	
	@Subscribe
	public void onPreWriteEvent(EntryModelPreWriteEvent evt) {
		getGuiData();
		formatDataForModel();
		setModelData();
	}
	
	@Subscribe
	public void onValidateEvent(EntryValidateEvent evt) {
		onValidate();
	}
	
	
	
	// model -> GUI
	protected void getModelData() {
	}
	protected void formatDataForGui() {
	}
	protected void setGuiData() {
	}

	

	// GUI -> model
	protected void getGuiData() {
	}
	protected void formatDataForModel() {
	}
	protected void setModelData() {
	}

	
	
	
	protected void onValidate() {
	}	


	protected void onModify() {
		if (ctlr!=null) ctlr.onModify();
		setHilite(HiliteMode.NONE);
	}
	
	protected void setHilite(HiliteMode mode) {
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
}
