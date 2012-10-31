package org.asianclassics.center.catalog.entry.cell;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell.BoxType;
import org.asianclassics.center.catalog.entry.model.DrawingModel;
import org.asianclassics.center.catalog.entry.model.Model;
import org.asianclassics.center.catalog.entry.model.PageModel;
import org.asianclassics.center.catalog.event.EntryCellListDeleteElementEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class DrawingEntryCell extends LinkedEntryCell implements ModelHoldingEntryCell {

	private TextEntryCell tecPage;
	private TextEntryCell tecCaption;
	private Button btnColors;
	private Button btnDelete;
	private DrawingModel model;


	public DrawingEntryCell(Composite parent, Model model) {
		super(parent, null);
		this.model = (DrawingModel) model;
		
		addHorizSep();
		
		tecPage = new TextEntryCell(this, "Page", 50, BoxType.SIMPLE, 100);
		FormData fd_tecBegin = new FormData();
		tecPage.setLayoutData(fd_tecBegin);

		tecCaption = new TextEntryCell(this, "Caption", 80);
		fd_tecBegin.left = new FormAttachment(0, 0);
		FormData fd_tecEnd = new FormData();
		fd_tecEnd.top = new FormAttachment(tecPage, 0, SWT.TOP);
		fd_tecEnd.left = new FormAttachment(tecPage, 6);
		tecCaption.setLayoutData(fd_tecEnd);

		btnColors = new Button(this, SWT.CHECK);
		FormData fd_btnColors = new FormData();
		fd_btnColors.left = new FormAttachment(tecCaption, 19);
		fd_btnColors.top = new FormAttachment(0, 10);
		fd_btnColors.bottom = new FormAttachment(0, 26);
		btnColors.setLayoutData(fd_btnColors);
		btnColors.setText("Has Colors");
		
		btnDelete = new Button(this, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onDelete();
			}
		});
		FormData fd_btnDelete_1 = new FormData();
		fd_btnDelete_1.left = new FormAttachment(tecPage, 341);
		fd_btnDelete_1.right = new FormAttachment(80);
		fd_btnDelete_1.bottom = new FormAttachment(btnColors, 25);
		fd_btnDelete_1.top = new FormAttachment(btnColors, 0, SWT.TOP);
		btnDelete.setLayoutData(fd_btnDelete_1);
		btnDelete.setText("Delete Drawing");
		
		onModelToView();
	}

	protected void onDelete() {
		eb.post(new EntryCellListDeleteElementEvent(this));
	}
	
	@Override
	public void onModelToView() {
		tecPage.setData(model.page);
		tecPage.onModelToView();
		tecCaption.setData(model.caption);
		tecCaption.onModelToView();
		btnColors.setSelection(model.hasColors);
	}

	@Override
	public void onViewToModel() {
		tecPage.onViewToModel();
		model.page = tecPage.getData();
		tecCaption.onViewToModel();
		model.caption = tecCaption.getData();
		model.hasColors = btnColors.getSelection();
	}
	
	@Override
	public Model getModel() {
		return (Model)model;
	}
	
	@Override
	public void dispose() {     ///  FIXME
//		eb.unregister(tecPage);
//		eb.unregister(tecCaption);
//		eb.unregister(this);
		super.dispose();
	}
}
