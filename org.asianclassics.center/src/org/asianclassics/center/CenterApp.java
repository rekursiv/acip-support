package org.asianclassics.center;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class CenterApp {

	/**
	 * Entry point
	 * @param args
	 */
	public static void main(String[] args) {
		CenterShell.setProperties();
		CenterApp instance = new CenterApp();
		instance.init();
	}
	
	protected void init() {
		Injector injector = Guice.createInjector(new CenterModule());
		
		CenterShell cs = new CenterShell();
		cs.setSize(800, 600);
		cs.setText("ACIP Input Center System Test");
		cs.setLayout(new FillLayout(SWT.HORIZONTAL));		
		
		CenterPanel cp = new CenterPanel(cs, injector);
		
		TaskView taskView = new DummyTaskView(cp.getMainStackView(), SWT.NONE, injector);
		cp.getMainStackView().setTaskView(taskView);
		
		cs.init(injector);

	}
	


}
