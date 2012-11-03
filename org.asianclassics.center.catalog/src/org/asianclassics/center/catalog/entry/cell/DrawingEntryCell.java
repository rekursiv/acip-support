package org.asianclassics.center.catalog.entry.cell;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell.BoxType;
import org.asianclassics.center.catalog.entry.model.DrawingModel;
import org.asianclassics.center.catalog.entry.model.Model;
import org.asianclassics.center.catalog.event.EntryCellListDeleteElementEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class DrawingEntryCell extends LinkedEntryCell implements ModelHoldingEntryCell {

	private TextEntryCell tecPage;
	private TextEntryCell tecCaption;
	private Button btnColors;
	private Button btnDelete;
	private DrawingModel model;
	private Label lblPosition;
	private Button btnLeft;
	private Button btnCenter;
	private Button btnRight;
	private Button btnFullPage;


	public DrawingEntryCell(Composite parent, Model model) {
		super(parent, null);
		this.model = (DrawingModel) model;
		
		tecPage = new TextEntryCell(this, "Page", 50, BoxType.SIMPLE, 100);
		FormData fd_tecPage = new FormData();
		fd_tecPage.left = new FormAttachment(0, 0);
		tecPage.setLayoutData(fd_tecPage);

		tecCaption = new TextEntryCell(this, "Caption", 70);
		FormData fd_tecCaption = new FormData();
		fd_tecCaption.left = new FormAttachment(0);
		fd_tecCaption.top = new FormAttachment(0, 37);
		tecCaption.setLayoutData(fd_tecCaption);

		btnColors = new Button(this, SWT.CHECK);
		FormData fd_btnColors = new FormData();
		btnColors.setLayoutData(fd_btnColors);
		btnColors.setText("Has Colors");
		
		btnDelete = new Button(this, SWT.NONE);
		fd_tecCaption.right = new FormAttachment(100);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onDelete();
			}
		});
		FormData fd_btnDelete = new FormData();
		fd_btnDelete.left = new FormAttachment(btnColors, 50);
		fd_btnDelete.top = new FormAttachment(0, 0);
		btnDelete.setLayoutData(fd_btnDelete);
		btnDelete.setText("Delete Drawing");
		
		lblPosition = new Label(this, SWT.NONE);
		fd_btnColors.bottom = new FormAttachment(lblPosition, 16);
		fd_btnColors.top = new FormAttachment(lblPosition, 0, SWT.TOP);
		FormData fd_lblPosition = new FormData();
		fd_lblPosition.top = new FormAttachment(0, 10);
//		fd_lblPosition.bottom = new FormAttachment(0, 27);
//		fd_lblPosition.right = new FormAttachment(0, 166);
		fd_lblPosition.left = new FormAttachment(0, 120);
		lblPosition.setLayoutData(fd_lblPosition);
		lblPosition.setText("Position:");
		
		btnLeft = new Button(this, SWT.RADIO);
		btnLeft.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onModify();
			}
		});
		FormData fd_btnLeft = new FormData();
		fd_btnLeft.top = new FormAttachment(lblPosition, -2, SWT.TOP);
		fd_btnLeft.left = new FormAttachment(lblPosition, 12);
		btnLeft.setLayoutData(fd_btnLeft);
		btnLeft.setText("Left");
		
		btnCenter = new Button(this, SWT.RADIO);
		btnCenter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onModify();
			}
		});
		FormData fd_btnCenter = new FormData();
		fd_btnCenter.left = new FormAttachment(btnLeft, 8);
		fd_btnCenter.top = new FormAttachment(btnLeft, 0, SWT.TOP);
		btnCenter.setLayoutData(fd_btnCenter);
		btnCenter.setText("Center");
		
		btnRight = new Button(this, SWT.RADIO);
		btnRight.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onModify();
			}
		});
		FormData fd_btnRight = new FormData();
		fd_btnRight.top = new FormAttachment(btnCenter, 0, SWT.TOP);
		fd_btnRight.left = new FormAttachment(btnCenter, 8);
		btnRight.setLayoutData(fd_btnRight);
		btnRight.setText("Right");
		
		btnFullPage = new Button(this, SWT.RADIO);
		btnFullPage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onModify();
			}
		});
		fd_btnColors.left = new FormAttachment(btnFullPage, 32);
		FormData fd_btnFullPage = new FormData();
		fd_btnFullPage.top = new FormAttachment(btnRight, 0, SWT.TOP);
		fd_btnFullPage.left = new FormAttachment(btnRight, 8);
		btnFullPage.setLayoutData(fd_btnFullPage);
		btnFullPage.setText("Full Page");
	
		addHorizSep();

		onModelToView();
	}
	
	@Override
	public void addHorizSep() {
		Label horizSep = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_horizSep = new FormData();
		fd_horizSep.top = new FormAttachment(tecCaption, 10);
		fd_horizSep.right = new FormAttachment(100, 0);
		fd_horizSep.left = new FormAttachment(0, 0);
		horizSep.setLayoutData(fd_horizSep);
	}

	protected void onDelete() {
		eb.post(new EntryCellListDeleteElementEvent(this));
	}
	
	
	@Override
	public void onModelToView() {
		tecPage.setData(model.page);
		tecPage.onModelToView();
		setPositionString(model.position);
		btnColors.setSelection(model.hasColors);
		tecCaption.setData(model.caption);
		tecCaption.onModelToView();
	}
	
	private void setPositionString(String pos) {
		if (pos==null) return;
		if (pos.equalsIgnoreCase("left")) btnLeft.setSelection(true);
		else if (pos.equalsIgnoreCase("center")) btnCenter.setSelection(true);
		else if (pos.equalsIgnoreCase("right")) btnRight.setSelection(true);
		else if (pos.equalsIgnoreCase("fullpage")) btnFullPage.setSelection(true);
	}
	
	

	@Override
	public void onViewToModel() {
		tecPage.onViewToModel();
		model.page = tecPage.getData();
		model.hasColors = btnColors.getSelection();
		model.position = getPositionString();
		tecCaption.onViewToModel();
		model.caption = tecCaption.getData();
	}
	
	private String getPositionString() {
		if (btnLeft.getSelection()==true) return "left";
		if (btnCenter.getSelection()==true) return "center";
		if (btnRight.getSelection()==true) return "right";
		if (btnFullPage.getSelection()==true) return "fullpage";
		return "unselected";
	}
	
	@Override
	protected void onValidate() {
		if (getPositionString().equals("unselected")) setHilite(HiliteMode.INVALID);
	}	
	
	@Override
	protected void setHilite(HiliteMode mode) {
		if (mode==HiliteMode.NONE) lblPosition.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		else if (mode==HiliteMode.INVALID) lblPosition.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
	}
	
	@Override
	public Model getModel() {
		return (Model)model;
	}
	
	@Override
	public void dispose() {
		eb.unregister(tecPage);
		eb.unregister(tecCaption);
		eb.unregister(this);
		super.dispose();
	}
}
