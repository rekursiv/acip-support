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
		scroller = new ScrolledComposite(this, SWT.V_SCROLL);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);	
		entryView = new EntryView(scroller, SWT.NONE, injector);
		scroller.setContent(entryView);
		
		onAdaptSize(null);
//		pack(); ///
		
		if (injector!=null) injector.injectMembers(this);
	}
	
	@Subscribe
	public void onAdaptSize(ParentAdaptSizeEvent evt) {
//		System.out.println("EntryScroller#onAdaptSize");
		scroller.setMinSize(entryView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
/*	
	@Override
	public void pack() {   															//  TODO:   maybe use the event bus instead???
		System.out.println("EntryScroller#pack");
		scroller.setMinSize(entryView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
*/
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
