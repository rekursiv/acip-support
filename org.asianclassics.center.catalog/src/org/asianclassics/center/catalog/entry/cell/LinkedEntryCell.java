package org.asianclassics.center.catalog.entry.cell;

import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.EntryValidateEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

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
	public void onValidateEvent(EntryValidateEvent evt) {
		onValidate();
	}
	
	@Subscribe
	public void onPreWrite(EntryModelPreWriteEvent evt) {
		onViewToModel();
	}
	
	// model -> GUI
	public void onModelToView() {
		getModelData();
		formatDataForGui();
		setGuiData();
	}
	protected void getModelData() {
	}
	protected void formatDataForGui() {
	}
	protected void setGuiData() {
	}

	

	// GUI -> model
	public void onViewToModel() {
		getGuiData();
		formatDataForModel();
		setModelData();
	}
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
		if (mode==HiliteMode.NONE) lblTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		else if (mode==HiliteMode.INVALID) lblTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		else if (mode==HiliteMode.COPIED) lblTitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
}
