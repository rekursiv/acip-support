package org.asianclassics.center.catalog.entry.cell;

import org.asianclassics.center.catalog.entry.EntryController;
import org.asianclassics.center.catalog.entry.EntryView;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridData;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

public class EntryCell extends Composite {

	public static final int defaultTitleWidth = 100;
	protected int titleWidth = defaultTitleWidth;
	protected String titleString = "Default";
	protected Label lblTitle;
	protected EntryController ctlr;
	protected EventBus eb;


	
	public EntryCell(Composite parent, String title) {
		this(parent, title, defaultTitleWidth);
	}
	
	public EntryCell(Composite parent, String title, int titleWidth) {
		super(parent, SWT.NONE);
		this.titleString = title;
		this.titleWidth = titleWidth;
		if (EntryView.getInstance()!=null) EntryView.getInstance().getInjector().injectMembers(this);
		
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		setLayout(new FormLayout());
		
		lblTitle = new Label(this, SWT.NONE);
		FormData fd_lblRow = new FormData();
		fd_lblRow.right = new FormAttachment(0, titleWidth);
		fd_lblRow.top = new FormAttachment(0, 10);
		fd_lblRow.left = new FormAttachment(0, 12);
		lblTitle.setLayoutData(fd_lblRow);
		lblTitle.setText(titleString+":");
		
	}
	
	
	@Inject
	public void inject(EventBus eb, EntryController ctlr) {
		this.eb = eb;
		this.ctlr = ctlr;
	}
	

	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
