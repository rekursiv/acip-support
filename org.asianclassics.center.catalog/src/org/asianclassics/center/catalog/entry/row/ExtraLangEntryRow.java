package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.ComboBoxEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;

public class ExtraLangEntryRow extends ComboBoxEntryCell {

	public ExtraLangEntryRow(Composite parent) {
		super(parent, "Extra Language");
		addHorizSep();
		combo.add("Lanycha script, Title page only");
		combo.add("Lanycha script, Whole sutra");
		combo.add("Mongolian script, Title page only");
		combo.add("Mongolian script, Whole sutra");
		fd_combo.right = new FormAttachment(lblTitle, 450);   //  Linux GTK won't resize comboboxes automagically
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().extraLanguage;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().extraLanguage = data;
	}

}
