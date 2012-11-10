package org.asianclassics.center.catalog.entry.cell;


import java.util.concurrent.atomic.AtomicInteger;

import org.asianclassics.center.catalog.event.EntryCellListDeleteElementEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class StampEntryCell extends LinkedEntryCell {

	private Text txtTest;
	
	private Button btnDelete;
	private AtomicInteger stampNum;


	public StampEntryCell(Composite parent, Object object) {
		super(parent, null);
		this.stampNum = (AtomicInteger) object;
		
		txtTest = new Text(this, SWT.NONE);
		FormData fd_txtTest = new FormData();
		fd_txtTest.right = new FormAttachment(0, 80);
		fd_txtTest.top = new FormAttachment(btnDelete, 6);
		fd_txtTest.left = new FormAttachment(0, 12);
		txtTest.setLayoutData(fd_txtTest);
		
		btnDelete = new Button(this, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onDelete();
			}
		});
		FormData fd_btnDelete_1 = new FormData();
		fd_btnDelete_1.left = new FormAttachment(txtTest, 50);
		fd_btnDelete_1.top = new FormAttachment(txtTest, -18);
		btnDelete.setLayoutData(fd_btnDelete_1);
		btnDelete.setText("Delete Stamp");
		
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
	}

	protected void onDelete() {
		eb.post(new EntryCellListDeleteElementEvent(this));
	}
	
	@Override
	public void onModelToView() {
		txtTest.setText(stampNum.toString());
	}

	@Override
	public void onViewToModel() {
		stampNum.set(Integer.parseInt(txtTest.getText()));
//		System.out.println("StampEntryCell#onViewToModel()   "+stampNum);
	}
	
	@Override
	public void dispose() {
//		System.out.println("StampEntryCell#dispose()");
		ctlr.getModel().stamps.remove(stampNum);
		eb.unregister(this);
		super.dispose();
	}
}
