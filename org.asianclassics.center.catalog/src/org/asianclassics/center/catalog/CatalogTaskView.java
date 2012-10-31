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

public class CatalogTaskView extends TaskView {
	private Button btnLogout;
	private Label lblWorkerId;
	private EventBus eb;
	private CatalogTaskStackView stack;

	
	public CatalogTaskView(Composite parent, int style) {
		super(parent, style);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public CatalogTaskView(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(new FormLayout());
		
		btnLogout = new Button(this, SWT.NONE);
		btnLogout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				eb.post(new LogoutEvent());
			}
		});
		FormData fd_btnLogout = new FormData();
		fd_btnLogout.right = new FormAttachment(0, 385);
		fd_btnLogout.left = new FormAttachment(0, 325);
		btnLogout.setLayoutData(fd_btnLogout);
		btnLogout.setText("Logout");
		
		lblWorkerId = new Label(this, SWT.NONE);
		fd_btnLogout.top = new FormAttachment(0, 10);
		fd_btnLogout.bottom = new FormAttachment(0, 40);
		FormData fd_lblWorkerId = new FormData();
		fd_lblWorkerId.bottom = new FormAttachment(btnLogout, 25);
		fd_lblWorkerId.top = new FormAttachment(btnLogout, 0, SWT.TOP);
		fd_lblWorkerId.right = new FormAttachment(0, 275);
		fd_lblWorkerId.left = new FormAttachment(0, 12);
		lblWorkerId.setLayoutData(fd_lblWorkerId);
		
		stack = new CatalogTaskStackView(this, SWT.NONE, injector);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, 0);
		fd_composite.right = new FormAttachment(100, 0);
		fd_composite.top = new FormAttachment(btnLogout, 6);
		fd_composite.left = new FormAttachment(0, 12);
		stack.setLayoutData(fd_composite);

		if (injector!=null) injector.injectMembers(this);
	}

	@Inject
	public void inject(EventBus eb) {
		this.eb = eb;
	}

	@Subscribe
	public void onLoginSuccess(LoginSuccessEvent evt) {
		lblWorkerId.setText("Worker ID:  "+evt.getWorkerId());
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.ENTRY));      /// /// ///    ENTRY / SELECTION
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
