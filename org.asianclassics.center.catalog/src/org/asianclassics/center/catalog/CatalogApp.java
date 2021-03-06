package org.asianclassics.center.catalog;

import org.asianclassics.center.CenterPanel;
import org.asianclassics.center.CenterShell;
import org.asianclassics.center.TaskView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class CatalogApp {


	/**
	 * Entry point
	 * @param args
	 */
	public static void main(String[] args) {
		CenterShell.setProperties();
		CatalogApp instance = new CatalogApp();
		instance.init();
	}
	
	protected void init() {
		CenterShell cs = new CenterShell();
		cs.setSize(1000, 1000);
		cs.setText("ACIP Input Center Catalog Entry");
		cs.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Injector injector = Guice.createInjector(new CatalogModule());
		
		CenterPanel cp = new CenterPanel(cs, injector);
		TaskView taskView = new CatalogTaskView(cp.getMainStackView(), SWT.NONE, injector);
		cp.getMainStackView().setTaskView(taskView);

		cs.init(injector);

	}
	

}
