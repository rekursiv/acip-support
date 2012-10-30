package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.ComboBoxEntryCell;
import org.eclipse.swt.widgets.Composite;

public class ExtraLangEntryRow extends ComboBoxEntryCell {

	public ExtraLangEntryRow(Composite parent) {
		super(parent, "Extra Language");
		combo.add("Lanycha script, Title page only");
		combo.add("Lanycha script, Whole sutra");
		combo.add("Mongolian script, Title page only");
		combo.add("Mongolian script, Whole sutra");
	}
	
	@Override
	protected void getModelData() {
		data = ctlr.getModel().extraLanguage2;
	}
	
	@Override
	protected void setModelData() {
		ctlr.getModel().extraLanguage2 = data;
	}

}
