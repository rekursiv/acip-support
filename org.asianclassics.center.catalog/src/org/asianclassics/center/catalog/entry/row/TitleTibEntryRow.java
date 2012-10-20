package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.TextEntryRow;
import org.asianclassics.center.catalog.event.EntryModelUpdateEvent;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;


public class TitleTibEntryRow extends TextEntryRow {

	public TitleTibEntryRow(Composite parent) {
		super(parent, "Tibetan Title", BoxType.STANDARD);
	}
	
	@Subscribe
	public void onModelUpdate(EntryModelUpdateEvent evt) {
		text.setText(ctlr.getModel().getTitleTibetan());
	}
	
}
