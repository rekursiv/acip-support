package org.asianclassics.center.catalog.entry.cell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class ComboBoxEntryCell extends StringEntryCell {
	protected Combo combo;
	protected boolean isReadOnly;


	public ComboBoxEntryCell(Composite parent, String title) {
		this(parent, title, false);
	}
	
	public ComboBoxEntryCell(Composite parent, String title, boolean isReadOnly) {
		super(parent, title);
		this.isReadOnly = isReadOnly;
		
		combo = new Combo(this, isReadOnly?SWT.READ_ONLY:SWT.NONE);
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				onModify();
			}
		});
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(lblTitle, 0, SWT.TOP);
		fd_combo.left = new FormAttachment(lblTitle, 6);
		combo.setLayoutData(fd_combo);
	}
	
	@Override
	protected void setGuiData() {
		if (data.isEmpty()) combo.deselectAll();
		else combo.setText(data);
	}
	
	@Override
	protected void getGuiData() {
		data = combo.getText();
	}
	
	@Override
	protected void onValidate() {
		if (combo.getText().isEmpty()) {
			ctlr.invalidate();
			setHilite(HiliteMode.INVALID);
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

