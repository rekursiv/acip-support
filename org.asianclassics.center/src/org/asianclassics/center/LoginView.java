package org.asianclassics.center;


import org.asianclassics.center.event.LinkReadyEvent;
import org.asianclassics.center.event.LoginFailureEvent;
import org.asianclassics.center.event.LoginRequestEvent;
import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.LogoutEvent;
import org.asianclassics.center.event.ServerHelpMessageEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;


public class LoginView extends Composite {
	private Button btnLogin;
	private Text txtWorkerId;
	private Label lblStatus;
	private EventBus eb;
	private Label lblLogo;
	private Label lblServerHelpMessage;

	
	public LoginView(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(new FormLayout());
		
		btnLogin = new Button(this, SWT.NONE);
		btnLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				login();
			}
		});
		FormData fd_btnLogin = new FormData();
		fd_btnLogin.right = new FormAttachment(0, 232);
		btnLogin.setLayoutData(fd_btnLogin);
		btnLogin.setText("Login");
		
		txtWorkerId = new Text(this, SWT.BORDER);
		fd_btnLogin.bottom = new FormAttachment(0, 143);
		txtWorkerId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode==SWT.CR) login();
				else {
					setStatusTextToBlank();
				}
			}
		});
		fd_btnLogin.left = new FormAttachment(0, 176);
		FormData fd_txtWorkerId = new FormData();
		fd_txtWorkerId.left = new FormAttachment(0, 7);
		fd_txtWorkerId.right = new FormAttachment(0, 170);
		txtWorkerId.setLayoutData(fd_txtWorkerId);
		
		lblStatus = new Label(this, SWT.NONE);
		fd_btnLogin.top = new FormAttachment(0, 115);
		FormData fd_lblStatus = new FormData();
		fd_lblStatus.bottom = new FormAttachment(txtWorkerId, 31, SWT.BOTTOM);
		fd_lblStatus.top = new FormAttachment(txtWorkerId, 6);
		fd_lblStatus.right = new FormAttachment(txtWorkerId, 348);
		fd_lblStatus.left = new FormAttachment(txtWorkerId, 0, SWT.LEFT);
		lblStatus.setLayoutData(fd_lblStatus);

		lblStatus.setText("status");
		
		lblLogo = new Label(this, SWT.NONE);
		fd_txtWorkerId.bottom = new FormAttachment(0, 144);
		fd_txtWorkerId.top = new FormAttachment(lblLogo, 6);
		lblLogo.setImage(SWTResourceManager.getImage(LoginView.class, "/org/asianclassics/center/logo_half.png"));
		lblLogo.setLayoutData(new FormData());
		
		lblServerHelpMessage = new Label(this, SWT.NONE);
		FormData fd_lblServerHelpMessage = new FormData();
		fd_lblServerHelpMessage.bottom = new FormAttachment(lblStatus, 25, SWT.BOTTOM);
		fd_lblServerHelpMessage.top = new FormAttachment(lblStatus);
		fd_lblServerHelpMessage.right = new FormAttachment(100, -12);
		fd_lblServerHelpMessage.left = new FormAttachment(0, 7);
		lblServerHelpMessage.setLayoutData(fd_lblServerHelpMessage);
		lblServerHelpMessage.setText("Please make sure that the server machine is powered on.");
//		Image img = new Image(Display.getDefault(), App.class.getResourceAsStream("logo_half.png"));
		
		if (injector!=null) injector.injectMembers(this);
	}
	
	@Inject
	public void inject(EventBus eb) {
		this.eb = eb;
		setIsReady(false);
	}
	
	@Subscribe
	public void onLinkReady(LinkReadyEvent evt) {
		setIsReady(true);
	}
	
	@Subscribe
	public void onLoginSuccess(LoginSuccessEvent evt) {
		setStatusText("Logging in '"+evt.getWorkerId()+"' ");
	}

	@Subscribe
	public void onLoginFailure(LoginFailureEvent evt) {
		setStatusText(evt.getErrorMessage());
	}
	
	@Subscribe
	public void onLogout(LogoutEvent evt) {
		reset();
	}
	
	@Subscribe
	public void onHelpUser(ServerHelpMessageEvent evt) {
		lblServerHelpMessage.setVisible(true);
	}
	
	private void login() {
		eb.post(new LoginRequestEvent(txtWorkerId.getText()));
	}
	
	public void setStatusTextToBlank() {
		lblStatus.setText("");
	}
	
	public void setStatusTextToInit() {
		lblStatus.setText("Enter your worker ID to log in.");
	}
	
	public void setStatusText(String txt) {
		lblStatus.setText(txt);
	}
	
	public void reset() {
		setStatusTextToInit();
		btnLogin.setVisible(true);
		txtWorkerId.setVisible(true);
		txtWorkerId.setText("");
		txtWorkerId.setFocus();
	}
	
	public void setIsReady(boolean isReady) {
		if (isReady) {
			reset();
		}
		else {
			txtWorkerId.setVisible(false);
			btnLogin.setVisible(false);
			lblStatus.setText("Connecting to database, please wait...");
		}
		lblServerHelpMessage.setVisible(false);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
