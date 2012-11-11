package org.asianclassics.center.catalog.entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.inject.Injector;

public class EntryScroller extends Composite {
	private ScrolledComposite scroller;
	private Composite entryView;

	public EntryScroller(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));		
		scroller = new ScrolledComposite(this, SWT.H_SCROLL|SWT.V_SCROLL);
		scroller.setExpandHorizontal(true);
		scroller.setShowFocusedControl(true);
		scroller.setMinWidth(900);
		entryView = new EntryView(scroller, SWT.NONE, injector);
		scroller.setContent(entryView);
		
		if (injector!=null) injector.injectMembers(this);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
