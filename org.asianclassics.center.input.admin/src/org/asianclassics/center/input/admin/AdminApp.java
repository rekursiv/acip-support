package org.asianclassics.center.input.admin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class AdminApp {

	/**
	 * Entry point
	 * @param args
	 */
	public static void main(String[] args) {
		AdminShell.setProperties();
		AdminApp instance = new AdminApp();
		instance.init();
	}
	
	protected void init() {
		Injector injector = Guice.createInjector(new AdminModule());
		
		AdminShell cs = new AdminShell();
		cs.setSize(800, 600);
		cs.setText("ACIP Input Center System Test");
		cs.setLayout(new FillLayout(SWT.HORIZONTAL));		
		
		RootPanel cp = new RootPanel(cs, injector);
		
//		TaskView taskView = new DummyTaskView(cp.getMainStackView(), SWT.NONE, injector);
//		cp.getMainStackView().setTaskView(taskView);
		
		cs.init(injector);

	}
	


}
