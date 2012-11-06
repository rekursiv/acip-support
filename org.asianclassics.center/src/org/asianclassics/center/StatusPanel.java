package org.asianclassics.center;

import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.LogoutEvent;
import org.asianclassics.center.event.StatusPanelUpdateEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;

public class StatusPanel extends Composite {
	private Label lblNodeType;
	private Label lblIp;
	private Label lblStatus;
	private Label sep3;
	private Label lblWorkerId;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StatusPanel(Composite parent, int style, Injector injector) {
		super(parent, SWT.BORDER);
		
		lblNodeType = new Label(this, SWT.NONE);
		lblNodeType.setBounds(4, 2, 61, 18);
		lblNodeType.setText("-");
		
		Label sep1 = new Label(this, SWT.SEPARATOR);
		sep1.setBounds(69, 0, 10, 21);
		
		lblIp = new Label(this, SWT.NONE);
		lblIp.setBounds(79, 2, 115, 18);
		lblIp.setText("-");
		
		Label sep2 = new Label(this, SWT.SEPARATOR);
		sep2.setBounds(197, 0, 10, 21);
		
		lblWorkerId = new Label(this, SWT.NONE);
		lblWorkerId.setBounds(209, 2, 99, 18);
		lblWorkerId.setText("-");
		
		sep3 = new Label(this, SWT.SEPARATOR);
		sep3.setBounds(308, 0, 10, 21);
		
		lblStatus = new Label(this, SWT.NONE);
		lblStatus.setBounds(320, 2, 779, 18);
		lblStatus.setText("-");

		if (injector!=null) injector.injectMembers(this);
	}
	
	
	
	@Subscribe
	public void onUpdate(StatusPanelUpdateEvent evt) {
		if (evt.getMessage()!=null) lblStatus.setText(evt.getMessage());
		if (evt.getNodeType()!=null) lblNodeType.setText(evt.getNodeType());
		if (evt.getIp()!=null) lblIp.setText(evt.getIp());
	}
	
	@Subscribe
	public void onLogin(LoginSuccessEvent evt) {
		lblWorkerId.setText(evt.getWorkerId());
	}
	
	@Subscribe
	public void onLogout(LogoutEvent evt) {
		lblWorkerId.setText("-");
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
