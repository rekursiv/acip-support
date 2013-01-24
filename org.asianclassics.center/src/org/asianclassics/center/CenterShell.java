package org.asianclassics.center;


import org.asianclassics.center.link.LinkManager;
import org.asianclassics.center.link.LoginController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;


import com.google.inject.Injector;

public class CenterShell extends Shell implements Listener {
	
	private LinkManager linkManager;
	private LoginController loginCtlr;
	
	
	public static void setProperties() {
		System.getProperties().setProperty("java.util.logging.config.class", "util.logging.LogSetup");
		System.getProperties().setProperty("java.net.preferIPv4Stack", "true");
	}


	public void init(Injector injector) {
		CenterConfig cfg = injector.getInstance(CenterConfig.class);
	
		if (cfg.allowCloseWhileLoggedIn==false) addListener(SWT.Close, this);
		if (cfg.openMaximized) setMaximized(true);
		
		if (CenterShell.class.getPackage().getImplementationVersion()!=null) {
			setText(getText()+" V"+CenterShell.class.getPackage().getImplementationVersion());
		}
		
		loginCtlr = injector.getInstance(LoginController.class);
		linkManager = injector.getInstance(LinkManager.class);
		linkManager.init();
		
		open();
		layout();
		
		Display display = Display.getDefault();
		while (!isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		if (loginCtlr!=null) loginCtlr.destroy();
		if (linkManager!=null) linkManager.destroy();
		SWTResourceManager.dispose();
	}
	
	public void handleEvent(Event event) {
		System.out.println("handleEvent");
		if (loginCtlr.isUserLoggedIn()) {
			MessageBox messageBox = new MessageBox(this, SWT.APPLICATION_MODAL|SWT.OK);
	        messageBox.setText("Shutdown while logged in attempted");
	        messageBox.setMessage("Please log out before quitting this application.");
	        messageBox.open();
	        event.doit = false;
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	
}
