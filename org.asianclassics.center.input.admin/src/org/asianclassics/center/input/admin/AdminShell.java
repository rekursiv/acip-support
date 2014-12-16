package org.asianclassics.center.input.admin;



import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.google.inject.Injector;

public class AdminShell extends Shell {

	
	public static void setProperties() {
		System.getProperties().setProperty("java.util.logging.config.class", "util.logging.LogSetup");
		System.getProperties().setProperty("java.net.preferIPv4Stack", "true");
	}


	public void init(Injector injector) {
		
		open();
		layout();
		
		Display display = Display.getDefault();
		while (!isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

//		SWTResourceManager.dispose();
	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	
}
