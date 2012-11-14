package org.asianclassics.center;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.asianclassics.center.config.AppConfig;
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
	
	private LinkManager linkManager;
	private LoginController loginCtlr;

	public void init(Injector injector) {
		AppConfig cfg = injector.getInstance(AppConfig.class);
		setupLogging(cfg);
	
		if (cfg.get().allowCloseWhileLoggedIn==false) addListener(SWT.Close, this);
		if (cfg.get().openMaximized) setMaximized(true);
		
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
	
	private void setupLogging(AppConfig cfg) {
		
		// setup logging to console
		if (cfg.get().logToConsole) {
			LogSetup.initConsole(Level.ALL);
		}

		// setup logging to file
		if (cfg.get().logToFile) {
			try {
				LogManager.getLogManager().getLogger("").addHandler(new FileHandler("%h/acipcenter-%u-%g.log", 0, 10));
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// log config errors
		cfg.logErrorsIfAny();
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	
}
