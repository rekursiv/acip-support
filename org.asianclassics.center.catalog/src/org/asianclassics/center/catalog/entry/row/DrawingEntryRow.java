package org.asianclassics.center.catalog.entry.row;

import java.util.LinkedList;
import java.util.List;

import org.asianclassics.center.catalog.entry.cell.DrawingEntryCell;
import org.asianclassics.center.catalog.entry.cell.EntryCell;
import org.asianclassics.center.catalog.entry.cell.ListEntryCell;
import org.asianclassics.center.catalog.entry.cell.MissingPageEntryCell;
import org.asianclassics.center.catalog.entry.cell.ModelHoldingEntryCell;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.entry.model.DrawingModel;
import org.asianclassics.center.catalog.entry.model.Model;
import org.asianclassics.center.catalog.entry.model.MissingPageModel;
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

public class DrawingEntryRow extends ListEntryCell {

	public DrawingEntryRow(Composite parent) {
		super(parent, "Drawings", "Add Drawing");
	}

	@Override
	protected void createNewModelListIfNeeded() {
		if (ctlr.getModel().drawings==null) ctlr.getModel().drawings = new LinkedList<DrawingModel>();
	}
	
	@Override
	protected Model createNewModel() {
		DrawingModel model = new DrawingModel();
		ctlr.getModel().drawings.add(model);
		return model;
	}
	
	@Override
	protected ModelHoldingEntryCell createNewView(Model model) {
		return new DrawingEntryCell(cellListPanel, model);
	}
	
	@Override
	protected void copyModelToView() {
		List<DrawingModel> drawings = ctlr.getModel().drawings;
		if (drawings==null) return;
		for (DrawingModel drawing : drawings) {
			addCellFromModel(drawing);
		}
	}

}
