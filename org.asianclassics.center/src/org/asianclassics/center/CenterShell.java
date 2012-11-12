package org.asianclassics.center;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.asianclassics.center.link.LinkManager;
import org.asianclassics.center.link.LoginController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import util.logging.LogSetup;

import com.google.inject.Injector;

public class CenterShell extends Shell implements Listener {
	
	public static final boolean showLogView = true;
	private static final boolean logToConsole = true;
	private static final boolean logToFile = true;	
	
	private LinkManager linkManager;
	private LoginController loginCtlr;
	
	public static void setProperties() {
		System.getProperties().setProperty("java.util.logging.config.class", "util.logging.LogSetup");
		System.getProperties().setProperty("java.net.preferIPv4Stack", "true");
		System.getProperties().setProperty("org.ektorp.support.AutoUpdateViewOnChange", "true");
	}

	public void init(Injector injector) {
//		addListener(SWT.Close, this);			////////////////////   TEST (window closing shortcut)
		setupLogging();
		
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
	
	private void setupLogging() {
		
		// setup logging to console
		if (logToConsole) {
			LogSetup.initConsole(Level.ALL);
		}

		// setup logging to file
		if (logToFile) {
			try {
				LogManager.getLogManager().getLogger("").addHandler(new FileHandler("%h/acipcenter-%u-%g.log", 0, 10));
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	
}
