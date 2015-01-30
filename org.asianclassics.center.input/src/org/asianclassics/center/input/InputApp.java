package org.asianclassics.center.input;

import org.asianclassics.center.CenterModule;
import org.asianclassics.center.CenterPanel;
import org.asianclassics.center.CenterShell;
import org.asianclassics.center.TaskView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class InputApp {

	/**
	 * Entry point
	 * @param args
	 */
	public static void main(String[] args) {
		CenterShell.setProperties();
		InputApp instance = new InputApp();
		instance.init();
//		instance.test();
	}
	
	protected void test() {
		Injector injector = Guice.createInjector(new CenterModule());
		InputTest it = injector.getInstance(InputTest.class);
		it.test();
	}
	
	protected void init() {
		CenterShell cs = new CenterShell();
		cs.setSize(2000, 1000);
		cs.setMinimumSize(1000, 700);
		cs.setText("ACIP Input Center Pecha Entry");
		cs.setLayout(new FillLayout(SWT.HORIZONTAL));
	
		Injector injector = Guice.createInjector(new CenterModule());
		
		InputTaskController itc = injector.getInstance(InputTaskController.class);
		itc.init();
	
		CenterPanel cp = new CenterPanel(cs, injector);
	
		TaskView taskView = new InputTaskView(cp.getMainStackView(), injector);
		cp.getMainStackView().setTaskView(taskView);
	
		cs.init(injector);  //  main loop
		
		itc.release();

	}


}
