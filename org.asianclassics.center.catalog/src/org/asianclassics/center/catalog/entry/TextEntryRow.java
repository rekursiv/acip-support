package org.asianclassics.center.catalog.entry;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class TextEntryRow extends EntryRow {
	protected static final int simpleBoxWidth = 300;
	protected enum BoxType {
		SIMPLE, STANDARD, WIDE
	}
	protected StyledText text;

	public TextEntryRow(final Composite parent, String rowLabel, String dbField, BoxType boxType) {
		super(parent, rowLabel, dbField);

		FormData fd_text = new FormData();

		if (boxType==BoxType.SIMPLE) {
			text = new StyledText(this, SWT.BORDER|SWT.SINGLE);
			fd_text.right = new FormAttachment(0, simpleBoxWidth);
			parent.pack();
		} else {
			text = new StyledText(this, SWT.BORDER|SWT.MULTI|SWT.WRAP);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent arg0) {
					parent.pack();
					parent.getParent().getParent().pack();
				}
			});
			fd_text.right = new FormAttachment(100, -12);
		}
		
		if (boxType==BoxType.WIDE) {
			fd_text.top = new FormAttachment(lblRow, 6);
			fd_text.left = new FormAttachment(0, 12);
		} else {
			fd_text.top = new FormAttachment(lblRow, -2, SWT.TOP);
			fd_text.left = new FormAttachment(lblRow, 6);
		}

		text.setLayoutData(fd_text);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

