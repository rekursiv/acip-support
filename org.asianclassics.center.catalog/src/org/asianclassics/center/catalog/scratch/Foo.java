package org.asianclassics.center.catalog.scratch;

import org.asianclassics.center.catalog.entry.EntryView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class Foo extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Foo(EntryView parent, String baz) {
		super(parent, SWT.NONE);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
