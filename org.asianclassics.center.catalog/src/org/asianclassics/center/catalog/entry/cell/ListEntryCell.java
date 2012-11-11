package org.asianclassics.center.catalog.entry.cell;

import java.util.LinkedList;
import java.util.List;

import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.asianclassics.center.catalog.event.EntryCellListDeleteElementEvent;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;

public abstract class ListEntryCell extends EntryCell {
	protected Composite rootPanel;
	protected Composite cellListPanel;
	protected Button btnAdd;
	protected List<LinkedEntryCell> cellList;

	public ListEntryCell(Composite parent, String title, String btnText) {
		super(parent, title);
		
		addHorizSep();
		
		rootPanel = new Composite(this, SWT.NONE);
		rootPanel.setLayout(new GridLayout(1, false));
		
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(100, -12);
		fd_composite.top = new FormAttachment(0, 0);
		fd_composite.left = new FormAttachment(lblTitle);
		rootPanel.setLayoutData(fd_composite);
		
		cellListPanel = new Composite(rootPanel, SWT.NONE);
		cellListPanel.setLayout(new GridLayout(1, false));
		
		btnAdd = new Button(rootPanel, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onAddNewCell();
			}
		});
		btnAdd.setText(btnText);
	}

	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		copyObjectsToView();
		eb.post(new ParentAdaptSizeEvent());
	}
	
	@Subscribe
	public void onDeleteCell(EntryCellListDeleteElementEvent evt) {
		System.out.println("ListEntryCell#onDeleteCell");
		if (cellList==null) return;
		LinkedEntryCell cell = evt.getCell();
		if (cell==null) return;
		if (cellList.remove(cell)) {
			cell.dispose();
			eb.post(new ParentAdaptSizeEvent());
		}
	}
	
	@Subscribe
	public void onMakeTop(CatalogTaskMakeTopEvent evt) {
		if (evt.getViewType()==CatalogTaskViewType.SELECTION) {
			deleteAllViews();
		}
	}
	
	protected void onAddNewCell() {
		// override me
	}
	
	protected LinkedEntryCell createNewView(Object object) {
		// override me
		return null;
	}
	
	protected void copyObjectsToView() {
		// override me
	}

	protected void addCellFromObject(Object object) {
		if (cellList==null) cellList = new LinkedList<LinkedEntryCell>();
		cellList.add(createNewView(object));
	}
	
	private void deleteAllViews() {
		if (cellList==null) return;
		for (LinkedEntryCell cell : cellList) {
			cell.dispose();
		}
		cellList = new LinkedList<LinkedEntryCell>();
		pack();
	}
	
}
