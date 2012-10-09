package org.asianclassics.center;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;

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

	public TaskView taskView;

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

	
	private void setupLogging() {
		
		// setup logging to console
//		LogSetup.initConsole(Level.ALL);

		// setup logging to file
		try {
			LogManager.getLogManager().getLogger("").addHandler(new FileHandler("%h/acipcenter-%u-%g.log", 0, 10));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(800, 600);
		shell.setText("ACIP Input Center System Test");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Injector injector = Guice.createInjector(new AppModule());
		
		CenterPanel cp = new CenterPanel(shell, SWT.NONE, injector);
		
		taskView = new DummyTaskView(cp.getMainStackView(), SWT.NONE, injector);
		cp.getMainStackView().setTaskView(taskView);
				
		setupLogging();
		
		injector.getInstance(LoginController.class);
		linkManager = injector.getInstance(LinkManager.class);
		linkManager.init();
//		linkManager.test();
	}

}
