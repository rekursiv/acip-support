package org.asianclassics.center.catalog.entry.cell;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell.BoxType;
import org.asianclassics.center.catalog.entry.model.MissingPageModel;
import org.asianclassics.center.catalog.event.EntryCellListDeleteElementEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Control;

public class MissingPageEntryCell extends LinkedEntryCell {

	private TextEntryCell tecBegin;
	private TextEntryCell tecEnd;
	private Button btnBlank;
	private Button btnDelete;
	private MissingPageModel missingPage;


	public MissingPageEntryCell(Composite parent, Object object) {
		super(parent, null);
		this.missingPage = (MissingPageModel) object;
		
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
		fd_btnDelete_1.left = new FormAttachment(btnBlank, 50);
		fd_btnDelete_1.top = new FormAttachment(tecEnd, -25);
		fd_btnDelete_1.bottom = new FormAttachment(tecEnd, 0, SWT.BOTTOM);
		btnDelete.setLayoutData(fd_btnDelete_1);
		btnDelete.setText("Delete Page");

		addHorizSep();
		
		onModelToView();
	}
	
	@Override
	public void addHorizSep() {
		Label horizSep = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_horizSep = new FormData();
		fd_horizSep.top = new FormAttachment(btnDelete, 10);
		fd_horizSep.right = new FormAttachment(100, 0);
		fd_horizSep.left = new FormAttachment(0, 0);
		horizSep.setLayoutData(fd_horizSep);
		setTabList(new Control[]{tecBegin, tecEnd, btnBlank});
	}

	protected void onDelete() {
		eb.post(new EntryCellListDeleteElementEvent(this));
	}
	
	@Override
	public void onModelToView() {
		tecBegin.setData(missingPage.begin);
		tecBegin.onModelToView();
		tecEnd.setData(missingPage.end);
		tecEnd.onModelToView();
		btnBlank.setSelection(missingPage.isBlank);
	}

	@Override
	public void onViewToModel() {
		tecBegin.onViewToModel();
		missingPage.begin = tecBegin.getData();
		tecEnd.onViewToModel();
		missingPage.end = tecEnd.getData();
		missingPage.isBlank = btnBlank.getSelection();
	}
	
	@Override
	public void dispose() {
		if (ctlr.getModel().missingPages!=null) ctlr.getModel().missingPages.remove(missingPage);
		eb.unregister(tecBegin);
		eb.unregister(tecEnd);
		eb.unregister(this);
		super.dispose();
	}
}
