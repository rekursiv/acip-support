package org.asianclassics.center;


import org.asianclassics.center.event.MainMakeTopEvent;
import org.asianclassics.center.event.MainMakeTopEvent.MainViewType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;

public class MainStackView extends Composite {

	private StackLayout stack;
	private TaskView taskView;
	private LoginView loginView;
	
	public MainStackView(Composite parent, int style, Injector injector) {
		super(parent, style);
		stack = new StackLayout();
		setLayout(stack);
		loginView = new LoginView(this, SWT.NONE, injector);
		stack.topControl = loginView;
		layout();
		if (injector!=null) injector.injectMembers(this);
	}
	
	public void setTaskView(TaskView tv) {
		taskView = tv;
	}
	
	@Subscribe
	public void onMakeTop(MainMakeTopEvent evt) {
		if (evt.getMvt()==MainViewType.LOGIN) stack.topControl = loginView;
		else if (evt.getMvt()==MainViewType.TASK) stack.topControl = taskView;
		layout();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
