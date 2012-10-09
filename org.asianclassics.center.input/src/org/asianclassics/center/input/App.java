package org.asianclassics.center.input;

import java.util.logging.Level;

//import org.asianclassics.center.CenterPanel;
import org.asianclassics.center.LoginController;
import org.asianclassics.center.TaskView;
import org.asianclassics.center.link.LinkManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import util.logging.LogSetup;
import org.eclipse.swt.layout.FillLayout;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class App {

	public static App instance;
	
	protected Shell shell;
	private LinkManager linkManager;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		System.getProperties().setProperty("java.util.logging.config.class", "util.logging.LogSetup");
		System.getProperties().setProperty("java.net.preferIPv4Stack", "true");
		try {
			instance = new App();
			instance.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		if (linkManager!=null) linkManager.destroy();
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(900, 500);
		shell.setText("org.asianclassics.center.input");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Injector injector = Guice.createInjector(new AppModule());
		
		CenterPanel cp = new CenterPanel(shell, SWT.NONE, injector);
		
		TaskView taskView = new InputTaskView(cp.getMainStackView(), SWT.NONE, injector);
		cp.getMainStackView().setTaskView(taskView);
		
		
		LogSetup.initConsole(Level.ALL);
		
		injector.getInstance(LoginController.class);
		linkManager = injector.getInstance(LinkManager.class);
		linkManager.init();
	}

}
