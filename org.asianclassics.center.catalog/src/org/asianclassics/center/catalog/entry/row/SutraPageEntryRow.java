package org.asianclassics.center.catalog.entry.row;

import org.asianclassics.center.catalog.entry.cell.EntryCell;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell;
import org.asianclassics.center.catalog.entry.cell.TextEntryCell.BoxType;
import org.asianclassics.center.catalog.entry.model.SutraPageModel;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;

public class SutraPageEntryRow extends EntryCell {


	public SutraPageEntryRow(Composite parent) {
		super(parent, "Sutra Pages");
		addHorizSep();

		TextEntryCell begin = new TextEntryCell(this, "Begin", 40, BoxType.SIMPLE, 100) {
			
			@Subscribe
			public void onPostRead(EntryModelPostReadEvent evt) {
				onModelToView();
			}
			
			@Override
			protected void getModelData() {
				if (ctlr.getModel().sutraPages==null) data = null;   // FIXME:  this should be done in the model
				else data = ctlr.getModel().sutraPages.begin;
			}
			
			@Override
			protected void setModelData() {
				if (ctlr.getModel().sutraPages==null) ctlr.getModel().sutraPages = new SutraPageModel();  // FIXME:  this should be done in the model
				ctlr.getModel().sutraPages.begin = data;
			}
		};
		FormData fd_begin = new FormData();
		fd_begin.top = new FormAttachment(lblTitle, -10, SWT.TOP);
		fd_begin.left = new FormAttachment(lblTitle, 0, SWT.RIGHT);
		begin.setLayoutData(fd_begin);

		
		TextEntryCell end = new TextEntryCell(this, "End", 30, BoxType.SIMPLE, 100) {
			
			@Subscribe
			public void onPostRead(EntryModelPostReadEvent evt) {
				onModelToView();
			}
			
			@Override
			protected void getModelData() {
				if (ctlr.getModel().sutraPages==null) data = null;    // FIXME:  this should be done in the model
				else data = ctlr.getModel().sutraPages.end;
			}
			
			@Override
			protected void setModelData() {
				if (ctlr.getModel().sutraPages==null) ctlr.getModel().sutraPages = new SutraPageModel();  // FIXME:  this should be done in the model
				ctlr.getModel().sutraPages.end = data;
			}
		};
		FormData fd_end = new FormData();
		fd_end.top = new FormAttachment(begin, 0, SWT.TOP);
		fd_end.left = new FormAttachment(begin, 15, SWT.RIGHT);
		end.setLayoutData(fd_end);

	}
}
