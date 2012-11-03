package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.event.ParentAdaptSizeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;

public class EntryScroller extends Composite {
	private ScrolledComposite scroller;
	private Composite entryView;


	public EntryScroller(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		scroller = new ScrolledComposite(this, SWT.H_SCROLL|SWT.V_SCROLL);
		scroller.setExpandHorizontal(true);
//		scroller.setExpandVertical(true);	
		entryView = new EntryView(scroller, SWT.NONE, injector);
		scroller.setContent(entryView);
		scroller.setShowFocusedControl(true);
		scroller.setMinWidth(900);
		
//		onAdaptSize(null);
//		entryView.computeSize(SWT.DEFAULT, SWT.DEFAULT);  /////////////
		
		if (injector!=null) injector.injectMembers(this);
	}
	
//	@Subscribe
//	public void onAdaptSize(ParentAdaptSizeEvent evt) {
//		scroller.setMinSize(entryView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
