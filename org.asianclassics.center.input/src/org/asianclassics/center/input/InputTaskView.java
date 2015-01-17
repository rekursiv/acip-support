package org.asianclassics.center.input;

import java.util.logging.Logger;

import org.asianclassics.center.TaskView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class InputTaskView extends TaskView {
	private Logger log;

	private ScanInputPairPanel pairPanel;
	private InputControlPanel pnlControl;
	private InputTestPanel pnlTest;
	private static final boolean testMode=true;

	
	public InputTaskView(Composite parent, Injector injector) {
		super(parent, SWT.NONE);
		setLayout(new FormLayout());
		
		int pairPanelTopPos = 70;
		if (testMode) pairPanelTopPos = 130;

		pairPanel = new ScanInputPairPanel(this, injector);
		FormData fd_pairPanel = new FormData();
		fd_pairPanel.top = new FormAttachment(0, pairPanelTopPos);
		fd_pairPanel.bottom = new FormAttachment(100, -12);
		fd_pairPanel.right = new FormAttachment(100, -12);
		fd_pairPanel.left = new FormAttachment(0, 12);
		pairPanel.setLayoutData(fd_pairPanel);
		
		pnlControl = new InputControlPanel(this, injector);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 52);
		fd_composite.right = new FormAttachment(100, -12);
		fd_composite.top = new FormAttachment(0, 12);
		fd_composite.left = new FormAttachment(0, 12);
		pnlControl.setLayoutData(fd_composite);
		

		if (testMode) {
			pnlTest = new InputTestPanel(this, injector);
			FormData fd_test = new FormData();
			fd_test.right = new FormAttachment(pnlControl, 0, SWT.RIGHT);
			fd_test.bottom = new FormAttachment(0, 120);
			fd_test.top = new FormAttachment(0, 58);
			fd_test.left = new FormAttachment(0, 12);
			pnlTest.setLayoutData(fd_test);
		}
		
		if (injector!=null) injector.injectMembers(this);
	}



	@Inject
	public void inject(Logger log) {
		this.log = log;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
