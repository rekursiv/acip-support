package org.asianclassics.center;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.asianclassics.center.link.LinkManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import util.logging.LogSetup;

import com.google.inject.Injector;

public class CenterShell extends Shell {
	
	private static final boolean logToConsole = true;
	private static final boolean logToFile = true;	
	
	private LinkManager linkManager;
	
	
	public static void setProperties() {
		System.getProperties().setProperty("java.util.logging.config.class", "util.logging.LogSetup");
		System.getProperties().setProperty("java.net.preferIPv4Stack", "true");
	}

	public void init(Injector injector) {
		setupLogging();
		
		injector.getInstance(LoginController.class);
		linkManager = injector.getInstance(LinkManager.class);
		linkManager.init();
//		linkManager.test();
		
		open();
		layout();
		
		Display display = Display.getDefault();
		while (!isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		if (linkManager!=null) linkManager.destroy();
		
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
