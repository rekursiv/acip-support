package org.asianclassics.center;

import org.eclipse.swt.widgets.Composite;


public class TaskView extends Composite {
	
	public TaskView(Composite parent, int style) {
		super(parent, style);
	}
		
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
