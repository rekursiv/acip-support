package org.asianclassics.center.catalog;


import org.asianclassics.center.catalog.entry.EntryScroller;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;

public class CatalogTaskStackView extends Composite {

	private StackLayout stack;
	private EntryScroller entry;
	
	public CatalogTaskStackView(Composite parent, int style, Injector injector) {
		super(parent, style);
		stack = new StackLayout();
		setLayout(stack);
		entry = new EntryScroller(this, SWT.NONE, injector);
		stack.topControl = entry;
		layout();
		if (injector!=null) injector.injectMembers(this);
	}
	
	
	/*
	
	@Subscribe
	public void onMakeTop(MainMakeTopEvent evt) {
		if (evt.getMvt()==MainViewType.LOGIN) stack.topControl = loginView;
		else if (evt.getMvt()==MainViewType.TASK) stack.topControl = taskView;
		layout();
	}
*/
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
