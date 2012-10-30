package org.asianclassics.center.catalog.entry.row;

import java.util.LinkedList;
import java.util.List;

import org.asianclassics.center.catalog.entry.cell.EntryCell;
import org.asianclassics.center.catalog.entry.cell.MissingPageEntryCell;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridLayout;

import com.google.common.eventbus.Subscribe;

public class MissingPageEntryRow extends EntryCell {
	protected Composite rootPanel;
	protected Composite cellListPanel;
	protected Button btnAdd;
	protected List<MissingPageEntryCell> cellList;

	public MissingPageEntryRow(Composite parent) {
		super(parent, "Missing Pages");
		
		rootPanel = new Composite(this, SWT.NONE);
		rootPanel.setLayout(new GridLayout(1, false));
		
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(100, -12);
		fd_composite.top = new FormAttachment(0, 12);
		fd_composite.left = new FormAttachment(lblTitle);
		rootPanel.setLayoutData(fd_composite);

		
		cellListPanel = new Composite(rootPanel, SWT.NONE);
		cellListPanel.setLayout(new GridLayout(1, false));
		
		btnAdd = new Button(rootPanel, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNew();
			}
		});
		btnAdd.setText("Add");

	}

	protected void addNew() {
		if (cellList==null) cellList = new LinkedList<MissingPageEntryCell>();
		cellList.add(new MissingPageEntryCell(cellListPanel));
		eb.post(new ParentAdaptSizeEvent());
	}
	
	@Subscribe
	public void onPostReadEvent(EntryModelPostReadEvent evt) {
		System.out.println("MissingPageEntryRow#onPostReadEvent");
	}
}
