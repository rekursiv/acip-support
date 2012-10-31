package org.asianclassics.center.catalog.entry.cell;

import java.util.LinkedList;
import java.util.List;

import org.asianclassics.center.catalog.entry.model.Model;
import org.asianclassics.center.catalog.entry.model.PageModel;
import org.asianclassics.center.catalog.event.EntryCellListDeleteElementEvent;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.asianclassics.center.catalog.event.TestEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;

public class ListEntryCell extends EntryCell {
	protected Composite rootPanel;
	protected Composite cellListPanel;
	protected Button btnAdd;
	protected List<ModelHoldingEntryCell> cellList;

	public ListEntryCell(Composite parent, String title, String btnText) {
		super(parent, title);
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
		btnAdd.setText(btnText);
	}

	protected void createNewModelListIfNeeded() {
	}
	
	protected Model createNewModel() {
		return null;
	}
	
	protected void copyModelToView() {

	}
	
	
	protected void onAddNewCell() {
		createNewModelListIfNeeded();
		Model model = createNewModel();
		addCellFromModel(model);
		eb.post(new ParentAdaptSizeEvent());
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		deleteAllViews();
		copyModelToView();
		eb.post(new ParentAdaptSizeEvent());
	}
	
	protected void addCellFromModel(Model model) {
		if (cellList==null) cellList = new LinkedList<ModelHoldingEntryCell>();
		cellList.add(new MissingPageEntryCell(cellListPanel, model));
	}
	
	protected void deleteAllViews() {
		if (cellList==null) return;
		for (ModelHoldingEntryCell cell : cellList) {
			cell.dispose();
		}
		cellList = new LinkedList<ModelHoldingEntryCell>();
		pack();
	}
	
	
	@Subscribe
	public void onDeleteCell(EntryCellListDeleteElementEvent evt) {
		System.out.println("MissingPageEntryRow#onDeleteCell");
		if (cellList==null) return;
		ModelHoldingEntryCell cell = evt.getCell();
		if (cell==null) return;
		Model model = cell.getModel();
//		PageModel page = (PageModel)((MissingPageEntryCell)cell).getModel();
		ctlr.getModel().missingPages.remove(model);
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
