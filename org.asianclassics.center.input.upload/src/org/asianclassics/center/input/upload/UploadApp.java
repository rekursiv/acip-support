package org.asianclassics.center.input.upload;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class UploadApp {

	/**
	 * Entry point
	 * @param args
	 */
	public static void main(String[] args) {
		UploadShell.setProperties();
		UploadApp instance = new UploadApp();
//		instance.init();
		instance.test();
	}
	

	protected void test() {
		Injector injector = Guice.createInjector(new UploadModule());
		UploadController uc = injector.getInstance(UploadController.class);
		uc.test();
	}
	
	protected void init() {
		Injector injector = Guice.createInjector(new UploadModule());
	
		UploadShell cs = new UploadShell();
		cs.setSize(800, 600);
		cs.setText("ACIP Input Center Scan Uploader");
		cs.setLayout(new FillLayout(SWT.HORIZONTAL));		
		
		RootPanel cp = new RootPanel(cs, injector);
		
//		TaskView taskView = new DummyTaskView(cp.getMainStackView(), SWT.NONE, injector);
//		cp.getMainStackView().setTaskView(taskView);
		
		cs.init(injector);

	}
	


}
