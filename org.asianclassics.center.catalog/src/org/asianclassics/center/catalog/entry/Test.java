package org.asianclassics.center.catalog.entry;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;


public class Test extends Composite {
	private Combo combo;


	public Test(Composite parent, int style) {
		super(parent, style);
		
		combo = new Combo(this, SWT.READ_ONLY);
		combo.setItems(new String[] {"wb", "test"});
		combo.setBounds(10, 10, 127, 23);
		combo.add("foo");
		combo.add("bar");
		combo.add("baz");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
