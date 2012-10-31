package org.asianclassics.center.catalog.entry.row;

import java.util.LinkedList;
import java.util.List;

import org.asianclassics.center.catalog.entry.cell.EntryCell;
import org.asianclassics.center.catalog.entry.cell.MissingPageEntryCell;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.entry.model.PageModel;
import org.asianclassics.center.catalog.event.EntryCellListDeleteElementEvent;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.asianclassics.center.catalog.event.TestEvent;
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
		
		addHorizSep();
		
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
				onAddNewCell();
			}
		});
		btnAdd.setText("Add Missing or Blank Page");

	}

	protected void onAddNewCell() {
		PageModel page = new PageModel();
		if (ctlr.getModel().missingPages==null) ctlr.getModel().missingPages = new LinkedList<PageModel>();
		ctlr.getModel().missingPages.add(page);
		addCellFromModel(page);
		eb.post(new ParentAdaptSizeEvent());
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		System.out.println("MissingPageEntryRow#onPostRead");
		deleteAllViews();
		List<PageModel> pages = ctlr.getModel().missingPages;
		if (pages==null) return;
		for (PageModel page : pages) {
			addCellFromModel(page);
		}
		eb.post(new ParentAdaptSizeEvent());
	}
	
	private void addCellFromModel(PageModel page) {
		if (cellList==null) cellList = new LinkedList<MissingPageEntryCell>();
		cellList.add(new MissingPageEntryCell(cellListPanel, page));
	}
	
	public void deleteAllViews() {
		if (cellList==null) return;
		for (MissingPageEntryCell cell : cellList) {
			cell.dispose();
		}
		cellList = new LinkedList<MissingPageEntryCell>();
		pack();
	}
	
	
	@Subscribe
	public void onDeleteCell(EntryCellListDeleteElementEvent evt) {
		System.out.println("MissingPageEntryRow#onDeleteCell");
		if (cellList==null) return;
		EntryCell cell = evt.getCell();
		if (cell==null) return;
		PageModel page = ((MissingPageEntryCell)cell).getModel();
		ctlr.getModel().missingPages.remove(page);
		cellList.remove(cell);
		cell.dispose();
		eb.post(new ParentAdaptSizeEvent());
	}
	

	
	
	
	@Subscribe
	public void onTest(TestEvent evt) {
		if (cellList==null) System.out.println("NULL");
		else {
			System.out.println("cl size="+cellList.size());
			System.out.println("m size="+ctlr.getModel().missingPages.size());
		}
	}
}
