package org.asianclassics.center.input.admin;

import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class AdminView extends Composite {

	private Logger log;
	private AdminController ctlr;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public AdminView(Composite parent, Injector injector) {
		super(parent, SWT.NONE);

		if (injector!=null) injector.injectMembers(this);
	}

	@Inject
	public void inject(Logger log, AdminController ctlr) {
		this.log = log;
		this.ctlr = ctlr;
		
		ctlr.test();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
