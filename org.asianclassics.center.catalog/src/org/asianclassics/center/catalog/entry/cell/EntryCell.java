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
	public enum HiliteMode {
		NONE, COPIED, INVALID
	}

	public static final int defaultTitleWidth = 100;
	protected int titleWidth = defaultTitleWidth;
	protected String title = "Default";
	protected Label lblTitle;
	protected EntryController ctlr;
	protected EventBus eb;

	/**
	 * @wbp.parser.constructor
	 */
	public EntryCell(Composite parent) {
		super(parent, SWT.NONE);
		buildGui();
	}
	
	public EntryCell(String title) {
		this(title, defaultTitleWidth);
	}
	
	public EntryCell(String title, int titleWidth) {
		super(EntryView.getInstance(), SWT.NONE);
		this.title = title;
		this.titleWidth = titleWidth;
	
		buildGui();
		EntryView.getInstance().getInjector().injectMembers(this);
	}
	
	
	protected void buildGui() {
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		setLayout(new FormLayout());
		
		lblTitle = new Label(this, SWT.NONE);
		FormData fd_lblRow = new FormData();
		fd_lblRow.right = new FormAttachment(0, titleWidth);
		fd_lblRow.top = new FormAttachment(0, 10);
		fd_lblRow.left = new FormAttachment(0, 12);
		lblTitle.setLayoutData(fd_lblRow);
		lblTitle.setText(title+":");		
	}
	
	@Inject
	public void inject(EventBus eb, EntryController ctlr) {
		this.eb = eb;
		this.ctlr = ctlr;
	}
	
	protected void onModify() {
		if (ctlr!=null) ctlr.onModify();
		setHilite(HiliteMode.NONE);
	}
	
	protected void setHilite(HiliteMode mode) {
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
