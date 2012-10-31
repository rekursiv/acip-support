package org.asianclassics.center.catalog.entry.row;

import java.util.LinkedList;
import java.util.List;

import org.asianclassics.center.catalog.entry.cell.EntryCell;
import org.asianclassics.center.catalog.entry.cell.ListEntryCell;
import org.asianclassics.center.catalog.entry.cell.MissingPageEntryCell;
import org.asianclassics.center.catalog.entry.cell.ModelHoldingEntryCell;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.entry.model.Model;
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

public class MissingPageEntryRow extends ListEntryCell {
	public MissingPageEntryRow(Composite parent) {
		super(parent, "Missing Pages", "Add Missing or Blank Page");
	}

	@Override
	protected void createNewModelListIfNeeded() {
		if (ctlr.getModel().missingPages==null) ctlr.getModel().missingPages = new LinkedList<PageModel>();
	}
	
	@Override
	protected Model createNewModel() {
		PageModel pm = new PageModel();
		ctlr.getModel().missingPages.add(pm);
		return pm;
	}
	
	@Override
	protected void copyModelToView() {
		List<PageModel> pages = ctlr.getModel().missingPages;
		if (pages==null) return;
		for (PageModel page : pages) {
			addCellFromModel(page);
		}
	}



}
