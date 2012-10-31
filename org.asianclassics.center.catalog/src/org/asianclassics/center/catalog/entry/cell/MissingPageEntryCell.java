package org.asianclassics.center.catalog.entry.cell;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell.BoxType;
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

public class MissingPageEntryCell extends LinkedEntryCell implements ModelHoldingEntryCell {

	private TextEntryCell tecBegin;
	private TextEntryCell tecEnd;
	private Button btnBlank;
	private Button btnDelete;
	private PageModel model;


	public MissingPageEntryCell(Composite parent, Model model) {
		super(parent, null);
		this.model = (PageModel) model;
		
		addHorizSep();
		
		tecBegin = new TextEntryCell(this, "Begin", 50, BoxType.SIMPLE, 100);
		FormData fd_tecBegin = new FormData();
		tecBegin.setLayoutData(fd_tecBegin);

		tecEnd = new TextEntryCell(this, "End", 40, BoxType.SIMPLE, 100);
		fd_tecBegin.left = new FormAttachment(0, 0);
		FormData fd_tecEnd = new FormData();
		fd_tecEnd.top = new FormAttachment(tecBegin, 0, SWT.TOP);
		fd_tecEnd.left = new FormAttachment(tecBegin, 6);
		tecEnd.setLayoutData(fd_tecEnd);

		btnBlank = new Button(this, SWT.CHECK);
		FormData fd_btnBlank = new FormData();
		fd_btnBlank.left = new FormAttachment(tecEnd, 19);
		fd_btnBlank.top = new FormAttachment(0, 10);
		fd_btnBlank.bottom = new FormAttachment(0, 26);
		btnBlank.setLayoutData(fd_btnBlank);
		btnBlank.setText("Blank");
		
		btnDelete = new Button(this, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onDelete();
			}
		});
		FormData fd_btnDelete_1 = new FormData();
		fd_btnDelete_1.left = new FormAttachment(btnBlank, 55);
		fd_btnDelete_1.top = new FormAttachment(tecEnd, -25);
		fd_btnDelete_1.bottom = new FormAttachment(tecEnd, 0, SWT.BOTTOM);
		btnDelete.setLayoutData(fd_btnDelete_1);
		btnDelete.setText("Delete Missing or Blank Page");
		
		onModelToView();
	}

	protected void onDelete() {
		eb.post(new EntryCellListDeleteElementEvent(this));
	}
	
	@Override
	public void onModelToView() {
		tecBegin.setData(model.begin);
		tecBegin.onModelToView();
		tecEnd.setData(model.end);
		tecEnd.onModelToView();
		btnBlank.setSelection(model.isBlank);
	}

	@Override
	public void onViewToModel() {
		tecBegin.onViewToModel();
		model.begin = tecBegin.getData();
		tecEnd.onViewToModel();
		model.end = tecEnd.getData();
		model.isBlank = btnBlank.getSelection();
	}
	
	@Override
	public Model getModel() {
		return (Model)model;
	}
	
	@Override
	public void dispose() {     ///  FIXME
//		eb.unregister(tecBegin);
//		eb.unregister(tecEnd);
//		eb.unregister(this);
		super.dispose();
	}
}
