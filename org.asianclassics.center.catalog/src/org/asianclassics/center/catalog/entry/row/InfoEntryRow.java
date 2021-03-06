package org.asianclassics.center.catalog.entry.row;

import java.util.Date;

import org.asianclassics.center.catalog.entry.cell.EntryCell;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


import com.google.common.eventbus.Subscribe;

public class InfoEntryRow extends EntryCell {
	private Label lblSerialTitle;
	private Label lblSerial;
	private Label lblSubDateTitle;
	private Label lblSubDate;
	private Label lblEditDateTitle;
	private Label lblEditDate;

	
	public InfoEntryRow(Composite parent) {
		super(parent, "Info");
		
		addHorizSep();
		
		lblSerialTitle = new Label(this, SWT.NONE);
		FormData fd_lblSerialTitle = new FormData();
		fd_lblSerialTitle.top = new FormAttachment(lblTitle, 0, SWT.TOP);
		fd_lblSerialTitle.left = new FormAttachment(lblTitle, 6);
		lblSerialTitle.setLayoutData(fd_lblSerialTitle);
		lblSerialTitle.setText("Serial #:");
		
		lblSerial = new Label(this, SWT.NONE);
		FormData fd_lblSerial = new FormData();
		fd_lblSerial.left = new FormAttachment(lblSerialTitle, 18);
		fd_lblSerial.top = new FormAttachment(lblSerialTitle, 0, SWT.TOP);
		lblSerial.setLayoutData(fd_lblSerial);
		lblSerial.setText("<       serial #      >");
		
		lblSubDateTitle = new Label(this, SWT.NONE);
		FormData fd_lblSubDateTitle = new FormData();
		fd_lblSubDateTitle.bottom = new FormAttachment(0, 45);
		fd_lblSubDateTitle.left = new FormAttachment(lblTitle, 6);
		fd_lblSubDateTitle.top = new FormAttachment(0, 30);
		lblSubDateTitle.setLayoutData(fd_lblSubDateTitle);
		lblSubDateTitle.setText("First submitted on:");
		
		lblSubDate = new Label(this, SWT.NONE);
		FormData fd_lblSubDate = new FormData();
		fd_lblSubDate.top = new FormAttachment(lblSubDateTitle, -15);
		fd_lblSubDate.bottom = new FormAttachment(lblSubDateTitle, 0, SWT.BOTTOM);
		fd_lblSubDate.left = new FormAttachment(lblSubDateTitle, 6);
		lblSubDate.setLayoutData(fd_lblSubDate);
		lblSubDate.setText("<                               date                                                                    >");
		
		lblEditDateTitle = new Label(this, SWT.NONE);
		lblEditDateTitle.setText("Last edited on:");
		FormData fd_lblEditDateTitle = new FormData();
		fd_lblEditDateTitle.top = new FormAttachment(lblSubDate, 6);
		fd_lblEditDateTitle.left = new FormAttachment(lblSubDateTitle, 0, SWT.LEFT);
		lblEditDateTitle.setLayoutData(fd_lblEditDateTitle);
		
		lblEditDate = new Label(this, SWT.NONE);
		lblEditDate.setText("<                               date                                                                    >");
		FormData fd_lblEditDate = new FormData();
		fd_lblEditDate.right = new FormAttachment(0, 547);
		fd_lblEditDate.top = new FormAttachment(lblSubDate, 6);
		fd_lblEditDate.left = new FormAttachment(lblSubDateTitle, 6);
		lblEditDate.setLayoutData(fd_lblEditDate);		
	}
	
	
	@Subscribe
	public void onModelPostRead(EntryModelPostReadEvent evt) {
		lblSerial.setText(ctlr.getModel().getSerial());
		lblSubDate.setText(formatDate(ctlr.getModel().dateTimeFirstSubmitted));
		lblEditDate.setText(formatDate(ctlr.getModel().dateTimeLastEdited));
	}
	
	private String formatDate(Date date) {
		return "FIXME";
//		if (dt==null) return "(not recorded)";
//		return dt.withZone(DateTimeZone.getDefault()).toString("d MMMM, y 'at' h:mm a"); 
	}
	

}
