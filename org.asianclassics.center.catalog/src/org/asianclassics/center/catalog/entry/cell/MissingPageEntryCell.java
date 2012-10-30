package org.asianclassics.center.catalog.entry.cell;

import org.asianclassics.center.catalog.entry.cell.TextEntryCell.BoxType;
import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class MissingPageEntryCell extends LinkedEntryCell {

	private TextEntryCell tecBegin;
	private TextEntryCell tecEnd;
	private Button btnBlank;
	private Button btnDelete;


	public MissingPageEntryCell(Composite parent) {
		super(parent, null);
		
		tecBegin = new TextEntryCell(this, "Begin", 50, BoxType.SIMPLE, 100);
		FormData fd_tecBegin = new FormData();
		tecBegin.setLayoutData(fd_tecBegin);

		tecEnd = new TextEntryCell(this, "End", 40, BoxType.SIMPLE, 100);
		fd_tecBegin.left = new FormAttachment(20, -55);
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
		fd_btnBlank.right = new FormAttachment(0, 310);
		FormData fd_btnDelete_1 = new FormData();
		fd_btnDelete_1.left = new FormAttachment(btnBlank, 55);
		fd_btnDelete_1.top = new FormAttachment(tecEnd, -25);
		fd_btnDelete_1.bottom = new FormAttachment(tecEnd, 0, SWT.BOTTOM);
		fd_btnDelete_1.right = new FormAttachment(0, 410);
		btnDelete.setLayoutData(fd_btnDelete_1);
		btnDelete.setText("Delete");
		
	}


	protected void onDelete() {   //  FIXME
		this.dispose();   // ??
		eb.post(new ParentAdaptSizeEvent());
	}
}
