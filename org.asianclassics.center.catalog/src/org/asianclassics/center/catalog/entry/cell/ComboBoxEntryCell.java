package org.asianclassics.center.catalog.entry.cell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class ComboBoxEntryCell extends StringEntryCell {
//	protected static final int boxWidth = 300;
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
//		fd_combo.right = new FormAttachment(0, boxWidth);
		fd_combo.top = new FormAttachment(lblTitle, 0, SWT.TOP);
		fd_combo.left = new FormAttachment(lblTitle, 6);
		combo.setLayoutData(fd_combo);
	}
	
	@Override
	protected void setGuiData() {
		combo.setText(data);
	}
	
	
	@Override
	protected void getGuiData() {
		data = combo.getText();
	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

