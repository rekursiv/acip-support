package org.asianclassics.center.catalog.entry;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;

public class EntryScroller extends Composite {
	private ScrolledComposite scroller;
	private Composite entryView;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public EntryScroller(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		scroller = new ScrolledComposite(this, SWT.V_SCROLL);
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);	
		entryView = new EntryView(scroller, SWT.NONE);
		scroller.setContent(entryView);
		pack();
	}
	
	@Override
	public void pack() {   															//  TODO:   maybe use the event bus instead???
//		System.out.println("EntryScroller#pack");
		scroller.setMinSize(entryView.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
