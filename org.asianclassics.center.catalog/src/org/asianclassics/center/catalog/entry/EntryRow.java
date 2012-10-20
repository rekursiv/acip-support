package org.asianclassics.center.catalog.entry;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridData;

import com.google.inject.Inject;

public class EntryRow extends Composite {
	private static final int labelWidth = 100;
	protected Label lblRow;
	protected EntryController ctlr;

	public EntryRow(Composite parent, String rowLabel) {
		super(parent, SWT.NONE);
		
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		setLayout(new FormLayout());
		
		lblRow = new Label(this, SWT.NONE);
		FormData fd_lblRow = new FormData();
		fd_lblRow.right = new FormAttachment(0, labelWidth);
		fd_lblRow.top = new FormAttachment(0, 10);
		fd_lblRow.left = new FormAttachment(0, 12);
		lblRow.setLayoutData(fd_lblRow);
		lblRow.setText(rowLabel+":");

		if (parent.getClass().isAssignableFrom((EntryView.class))) {
			((EntryView)getParent()).getInjector().injectMembers(this);
		}
	}
	
	@Inject
	public void inject(EntryController ctlr) {
		this.ctlr = ctlr;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
