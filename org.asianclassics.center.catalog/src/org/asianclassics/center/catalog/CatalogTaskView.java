package org.asianclassics.center.catalog;

import org.asianclassics.center.TaskView;
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
	private Label lblGenericTaskPanel;
	private EventBus eb;

	
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
		fd_btnLogout.right = new FormAttachment(0, 210);
		fd_btnLogout.bottom = new FormAttachment(0, 110);
		fd_btnLogout.top = new FormAttachment(0, 80);
		fd_btnLogout.left = new FormAttachment(0, 150);
		btnLogout.setLayoutData(fd_btnLogout);
		btnLogout.setText("Logout");
		
		lblGenericTaskPanel = new Label(this, SWT.NONE);
		FormData fd_lblGenericTaskPanel = new FormData();
		fd_lblGenericTaskPanel.bottom = new FormAttachment(20);
		fd_lblGenericTaskPanel.right = new FormAttachment(0, 395);
		fd_lblGenericTaskPanel.top = new FormAttachment(0, 35);
		fd_lblGenericTaskPanel.left = new FormAttachment(0, 55);
		lblGenericTaskPanel.setLayoutData(fd_lblGenericTaskPanel);

		if (injector!=null) injector.injectMembers(this);
	}

	@Inject
	public void inject(EventBus eb) {
		this.eb = eb;
	}

	@Subscribe
	public void onLoginSuccess(LoginSuccessEvent evt) {
		lblGenericTaskPanel.setText("CatalogTaskView:   user= '"+evt.getWorkerId()+"'");
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
