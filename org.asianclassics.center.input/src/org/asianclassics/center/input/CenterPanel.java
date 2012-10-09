package org.asianclassics.center.input;

import java.util.logging.Level;

import org.asianclassics.center.MainStackView;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

import com.google.inject.Injector;

import util.logging.LogSetup;
import util.logging.LogView;

public class CenterPanel extends Composite {
	private MainStackView mainStackView;
	private LogView logView;


	public CenterPanel(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(new FormLayout());
		
		mainStackView = new MainStackView(this, SWT.NONE, injector);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 215);
		fd_composite.right = new FormAttachment(100, -12);
		fd_composite.top = new FormAttachment(0, 12);
		fd_composite.left = new FormAttachment(0, 12);
		mainStackView.setLayoutData(fd_composite);
		
		logView = new LogView(this, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(100, -12);
		fd_composite_1.right = new FormAttachment(100, -12);
		fd_composite_1.top = new FormAttachment(mainStackView, 6);
		fd_composite_1.left = new FormAttachment(0, 12);
		logView.setLayoutData(fd_composite_1);

		LogSetup.initView(logView, Level.ALL);
	}
	
	public MainStackView getMainStackView() {
		return mainStackView;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
