package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.ComboBoxEntryRow;
import org.eclipse.swt.widgets.Composite;

public class ExtraLangEntryRow extends ComboBoxEntryRow {

	public ExtraLangEntryRow(Composite parent) {
		super(parent, "Extra Language", false);
		combo.add("Lanycha script, Title page only");
		combo.add("Lanycha script, Whole sutra");
		combo.add("Mongolian script, Title page only");
		combo.add("Mongolian script, Whole sutra");
	}
	
	

}
