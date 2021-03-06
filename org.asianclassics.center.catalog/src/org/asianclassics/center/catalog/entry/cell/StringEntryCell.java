package org.asianclassics.center.catalog.entry.cell;

import org.eclipse.swt.widgets.Composite;

public class StringEntryCell extends LinkedEntryCell {

	protected String data = "";
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public StringEntryCell(Composite parent, String title) {
		super(parent, title);
	}
	
	public StringEntryCell(Composite parent, String title, int titleWidth) {
		super(parent, title, titleWidth);
	}

	@Override
	protected void formatDataForGui() {
		if (data==null||data.isEmpty()) {
			if (ctlr.getModel().isValid) data="N";
			else data = "";
		}
	}
	
	@Override
	protected void formatDataForModel() {
		if (data.isEmpty()||data.compareToIgnoreCase("N")==0) data=null;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
