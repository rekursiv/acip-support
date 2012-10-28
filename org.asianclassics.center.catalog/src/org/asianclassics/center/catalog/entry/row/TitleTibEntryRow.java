package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.TextEntryRow;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.EntryValidateEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.SWT;


public class TitleTibEntryRow extends TextEntryRow {

	public TitleTibEntryRow(Composite parent) {
		super(parent, "Tibetan Title", BoxType.STANDARD);
	}
	
	@Override
	protected String getModelData() {
		return ctlr.getModel().titleTibetan;
	}
	
	@Override
	protected void setModelData(String data) {
		ctlr.getModel().titleTibetan = data;
	}
}
