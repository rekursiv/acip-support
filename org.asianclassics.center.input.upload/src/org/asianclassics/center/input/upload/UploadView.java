package org.asianclassics.center.input.upload;

import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class UploadView extends Composite {

	private Logger log;
	private UploadController ctlr;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public UploadView(Composite parent, Injector injector) {
		super(parent, SWT.NONE);

		if (injector!=null) injector.injectMembers(this);
	}

	@Inject
	public void inject(Logger log, UploadController ctlr) {
		this.log = log;
		this.ctlr = ctlr;
		
		ctlr.test();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
