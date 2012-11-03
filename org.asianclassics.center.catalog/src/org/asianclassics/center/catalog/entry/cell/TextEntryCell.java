package org.asianclassics.center.catalog.entry.cell;


import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;



public class TextEntryCell extends StringEntryCell {

	public enum BoxType {
		SIMPLE, STANDARD, WIDE
	}
	
	protected static final int defaultSimpleBoxWidth = 300;
	protected static final BoxType defaultBoxType = BoxType.STANDARD;

	protected StyledText text;
	protected int prevLineCount = 1;
	protected boolean adaptBoxHeight;
	protected int simpleBoxWidth;
	protected BoxType boxType;
	
	public TextEntryCell(Composite parent, String title) {
		this(parent, title, defaultTitleWidth, defaultBoxType, defaultSimpleBoxWidth);
	}
	
	public TextEntryCell(Composite parent, String title, int titleWidth) {
		this(parent, title, titleWidth, defaultBoxType, defaultSimpleBoxWidth);
	}

	public TextEntryCell(Composite parent, String title, BoxType boxType) {
		this(parent, title, defaultTitleWidth, boxType, defaultSimpleBoxWidth);
	}
	
	public TextEntryCell(Composite parent, String title, int titleWidth, BoxType boxType) {
		this(parent, title, titleWidth, boxType, defaultSimpleBoxWidth);
	}
	
	public TextEntryCell(Composite parent, String title, int titleWidth, BoxType boxType, int simpleBoxWidth) {
		
		super(parent, title, titleWidth);
		this.boxType = boxType;
		
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
					System.out.println(prevLineCount+":"+text.getLineCount());
					if (text.getLineCount()!=prevLineCount) eb.post(new ParentAdaptSizeEvent());
					prevLineCount=text.getLineCount();
				}
			}
		});
		
		text.addTraverseListener(new TraverseListener() {
		    public void keyTraversed(TraverseEvent e) {
		        if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
		            e.doit = true;
		        }
		    }
		});

		text.setLayoutData(fd_text);
	}
	
	
	@Override
	protected void setGuiData() {
		prevLineCount = 1;
		text.setText(data);
	}
	
	
	@Override
	protected void getGuiData() {
		data = text.getText();
	}
	

	@Override
	protected void onValidate() {
		if (text.getText().isEmpty()) {
			ctlr.invalidate();
			setHilite(HiliteMode.INVALID);
		}
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
