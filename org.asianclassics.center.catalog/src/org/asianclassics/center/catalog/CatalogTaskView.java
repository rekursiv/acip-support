package org.asianclassics.center.catalog;

import org.asianclassics.center.TaskView;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.LogoutEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Control;

public class CatalogTaskView extends TaskView {
	private Button btnLogout;
	private Label lblWorkerId;
	private EventBus eb;
	private CatalogTaskStackView stack;
	private Group group;

	
	public CatalogTaskView(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(new FormLayout());
		FormData fd_btnLogout = new FormData();
		FormData fd_lblWorkerId = new FormData();
		fd_lblWorkerId.right = new FormAttachment(0, 200);
		fd_lblWorkerId.left = new FormAttachment(0, 12);
		
		stack = new CatalogTaskStackView(this, SWT.NONE, injector);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, -12);
		fd_composite.right = new FormAttachment(100, 0);
		stack.setLayoutData(fd_composite);
		
		group = new Group(this, SWT.NONE);
		fd_composite.left = new FormAttachment(group, 0, SWT.LEFT);
		FormData fd_group = new FormData();
		fd_group.top = new FormAttachment(0, 0);
		fd_group.bottom = new FormAttachment(0, 55);
		fd_group.left = new FormAttachment(0, 12);
		fd_group.right = new FormAttachment(100, -12);
		group.setLayoutData(fd_group);
		group.setLayout(new FormLayout());
		
		btnLogout = new Button(group, SWT.NONE);
		btnLogout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				eb.post(new LogoutEvent());
			}
		});
		btnLogout.setLayoutData(fd_btnLogout);
		btnLogout.setText("Logout");
		
		lblWorkerId = new Label(group, SWT.NONE);
		fd_btnLogout.bottom = new FormAttachment(lblWorkerId, 30);
		fd_btnLogout.top = new FormAttachment(lblWorkerId, 0, SWT.TOP);
		fd_btnLogout.right = new FormAttachment(lblWorkerId, 66, SWT.RIGHT);
		fd_btnLogout.left = new FormAttachment(lblWorkerId, 6);
		fd_lblWorkerId.bottom = new FormAttachment(btnLogout, 25);
		fd_lblWorkerId.top = new FormAttachment(btnLogout, 0, SWT.TOP);
		lblWorkerId.setLayoutData(fd_lblWorkerId);
		fd_composite.top = new FormAttachment(group, 0);
		setTabList(new Control[]{stack});

		if (injector!=null) injector.injectMembers(this);
	}

	@Inject
	public void inject(EventBus eb) {
		this.eb = eb;
	}

	@Subscribe
	public void onLoginSuccess(LoginSuccessEvent evt) {
		lblWorkerId.setText("Worker ID:  "+evt.getWorkerId());
		if (CatalogApp.debugMode) eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.ENTRY));      /// /// ///    ENTRY / SELECTION
		else eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.SELECTION)); 
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
