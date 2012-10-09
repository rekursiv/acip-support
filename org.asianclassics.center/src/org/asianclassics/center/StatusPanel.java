package org.asianclassics.center;

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

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public StatusPanel(Composite parent, int style, Injector injector) {
		super(parent, SWT.BORDER);
		
		lblNodeType = new Label(this, SWT.NONE);
		lblNodeType.setBounds(10, 2, 55, 18);
		lblNodeType.setText("-");
		
		Label sep1 = new Label(this, SWT.SEPARATOR);
		sep1.setBounds(69, 0, 20, 21);
		
		lblIp = new Label(this, SWT.NONE);
		lblIp.setBounds(88, 2, 112, 18);
		lblIp.setText("-");
		
		Label sep2 = new Label(this, SWT.SEPARATOR);
		sep2.setBounds(197, 0, 20, 21);
		
		lblStatus = new Label(this, SWT.NONE);
		lblStatus.setBounds(217, 2, 882, 18);
		lblStatus.setText("-");

		if (injector!=null) injector.injectMembers(this);
	}
	
	
	
	@Subscribe
	public void onUpdate(StatusPanelUpdateEvent evt) {
		if (evt.message!=null) lblStatus.setText(evt.message);
		if (evt.nodeType!=null) lblNodeType.setText(evt.nodeType);
		if (evt.ip!=null) lblIp.setText(evt.ip);
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	
}
