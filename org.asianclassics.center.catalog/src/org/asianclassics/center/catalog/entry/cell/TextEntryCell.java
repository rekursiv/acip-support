package org.asianclassics.center.catalog.entry.cell;


import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.wb.swt.SWTResourceManager;



public class TextEntryCell extends LinkedEntryCell {

	public enum BoxType {
		SIMPLE, STANDARD, WIDE
	}
	
	protected static final int simpleBoxWidth = 300;
	protected static final BoxType defaultBoxType = BoxType.STANDARD;

	protected StyledText text;
	protected boolean adaptBoxHeight;
	protected BoxType boxType;
	
	public TextEntryCell(String title) {
		super(title);
	}
	
	public TextEntryCell(String title, int titleWidth) {
		super(title, titleWidth);
	}
	
	public TextEntryCell(String title, BoxType boxType) {
		super(title);
		this.boxType = boxType;
	}
	
	public TextEntryCell(String title, int titleWidth, BoxType boxType) {
		super(title, titleWidth);
		this.boxType = boxType;
	}
	
	@Override
	protected void buildGui() {
		super.buildGui();
		
		FormData fd_text = new FormData();

		if (boxType==BoxType.SIMPLE) {
			adaptBoxHeight = false;
			text = new StyledText(this, SWT.BORDER|SWT.SINGLE);
			fd_text.right = new FormAttachment(0, simpleBoxWidth);
//			parent.pack();   ///  FIXME    ->  eb.post(new ParentAdaptSizeEvent());    ->  TEST
		} else {
			adaptBoxHeight = true;
			text = new StyledText(this, SWT.BORDER|SWT.MULTI|SWT.WRAP);
			fd_text.right = new FormAttachment(100, -12);
		}
		
		if (boxType==BoxType.WIDE) {
			fd_text.top = new FormAttachment(lblTitle, 6);
			fd_text.left = new FormAttachment(0, 12);
		} else {
			fd_text.top = new FormAttachment(lblTitle, -2, SWT.TOP);
			fd_text.left = new FormAttachment(lblTitle, 6);
		}
		
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent evt) {
				onModify();
				if (adaptBoxHeight) {
					eb.post(new ParentAdaptSizeEvent());
				}
			}
		});

		text.setLayoutData(fd_text);
	}
	
	
	@Override
	protected void onPostRead() {
		String data = getModelData();
		if (data==null||data.isEmpty()) data="N";
		text.setText(data);
	}
	
	@Override
	protected String getModelData() {
		return null;
	}
	
	@Override
	protected void onPreWrite() {
		String data = text.getText();
		if (data.isEmpty()||data.compareToIgnoreCase("N")==0) data=null;
		setModelData(data);
	}	
	
	@Override
	protected void setModelData(String data) {
	}
	
	@Override
	protected void onValidate() {
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


	
}
