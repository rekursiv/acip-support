package org.asianclassics.center.catalog;


import org.asianclassics.center.catalog.entry.EntryScroller;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;

public class CatalogTaskStackView extends Composite {

	private StackLayout stack;
	private EntryScroller entry;
	private SelectionView selection;
	
	public CatalogTaskStackView(Composite parent, int style, Injector injector) {
		super(parent, style);
		stack = new StackLayout();
		setLayout(stack);
		selection = new SelectionView(this, SWT.NONE, injector);
		entry = new EntryScroller(this, SWT.NONE, injector);
		
		stack.topControl = selection;
		layout();
		
		if (injector!=null) injector.injectMembers(this);
	}
	
	
	@Subscribe
	public void onMakeTop(CatalogTaskMakeTopEvent evt) {
		if (evt.getViewType()==CatalogTaskViewType.SELECTION) stack.topControl = selection;
		else if (evt.getViewType()==CatalogTaskViewType.ENTRY) stack.topControl = entry;
		layout();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
