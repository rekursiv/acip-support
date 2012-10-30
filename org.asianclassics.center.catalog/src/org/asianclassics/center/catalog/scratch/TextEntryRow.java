package org.asianclassics.center.catalog.scratch;

import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.EntryValidateEvent;
import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.asianclassics.center.catalog.scratch.EntryRow.HiliteMode;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.common.eventbus.Subscribe;

public class TextEntryRow extends EntryRow {
	protected static final int simpleBoxWidth = 300;
	public enum BoxType {
		SIMPLE, STANDARD, WIDE
	}
	protected StyledText text;
	protected boolean adaptBoxHeight;

	public TextEntryRow(final Composite parent, String rowLabel, BoxType boxType) {
		super(parent, rowLabel);

		FormData fd_text = new FormData();

		if (boxType==BoxType.SIMPLE) {
			adaptBoxHeight = false;
			text = new StyledText(this, SWT.BORDER|SWT.SINGLE);
			fd_text.right = new FormAttachment(0, simpleBoxWidth);
			parent.pack();
		} else {
			adaptBoxHeight = true;
			text = new StyledText(this, SWT.BORDER|SWT.MULTI|SWT.WRAP);
			fd_text.right = new FormAttachment(100, -12);
		}
		
		if (boxType==BoxType.WIDE) {
			fd_text.top = new FormAttachment(lblRow, 6);
			fd_text.left = new FormAttachment(0, 12);
		} else {
			fd_text.top = new FormAttachment(lblRow, -2, SWT.TOP);
			fd_text.left = new FormAttachment(lblRow, 6);
		}
		
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent evt) {
				onModify();
				if (adaptBoxHeight) {
					parent.pack();
					eb.post(new ParentAdaptSizeEvent());
				}
			}
		});

		text.setLayoutData(fd_text);

	}

	
	@Subscribe
	public void onModelPostRead(EntryModelPostReadEvent evt) {
		String data = getModelData();
		if (data==null||data.isEmpty()) data="N";
		text.setText(data);
	}
	
	protected String getModelData() {  //  override me
		return null;
	}
	
	
	@Subscribe
	public void onModelPreWrite(EntryModelPreWriteEvent evt) {
		String data = text.getText();
		if (data.isEmpty()||data.compareToIgnoreCase("N")==0) data=null;
		setModelData(data);
	}
	
	protected void setModelData(String data) {  // override me
	}
	
	
	@Subscribe
	public void onValidate(EntryValidateEvent evt) {
		validate();
	}

	protected void validate() {
		if (isValid()) {
			// TODO:  
		} else {
			ctlr.invalidate();
			setHilite(HiliteMode.INVALID);
		}
	}
	
	protected boolean isValid() {
		if (text.getText().isEmpty()) return false;
		else return true;
	}
	
	@Override
	protected void setHilite(HiliteMode mode) {
		if (mode==HiliteMode.NONE) text.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		else if (mode==HiliteMode.INVALID) text.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		else if (mode==HiliteMode.COPIED) text.setBackground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}

