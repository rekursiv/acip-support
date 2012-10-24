package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.TextEntryRow;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.EntryValidateEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;


public class TitleSktEntryRow extends TextEntryRow {

	public TitleSktEntryRow(Composite parent) {
		super(parent, "Sanskrit Title", BoxType.STANDARD);
	}
	
	@Override
	protected String getModelData() {
		return ctlr.getModel().getTitleSanskrit();
	}
	
	@Override
	protected void setModelData(String data) {
		ctlr.getModel().setTitleSanskrit(data);
	}
}
