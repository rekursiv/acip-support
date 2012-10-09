package org.asianclassics.center;

import java.util.logging.Level;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

import com.google.inject.Injector;

import util.logging.LogSetup;
import util.logging.LogView;
import org.eclipse.swt.layout.FillLayout;

public class CenterPanel extends Composite {
	private MainStackView mainStackView;
	private LogView logView;

	boolean showLogView = false;
	private Composite statusPanel;

	public CenterPanel(Composite parent, int style, Injector injector) {
		super(parent, style);
		
		mainStackView = new MainStackView(this, SWT.NONE, injector);

		setLayout(new FormLayout());

		int statusPanelTop = 12;
		int statusPanelHeight = 25;
		
//		if (true) {  // for WindowBuilder	
		if (showLogView) {
			logView = new LogView(this, SWT.NONE);
			FormData fd_logView = new FormData();
			fd_logView.right = new FormAttachment(100, -12);
			fd_logView.bottom = new FormAttachment(0, 130);
			fd_logView.top = new FormAttachment(0, 12);
			fd_logView.left = new FormAttachment(0, 12);
			logView.setLayoutData(fd_logView);
			LogSetup.initView(logView, Level.ALL);
			statusPanelTop = 136;
		}
		
		statusPanel = new StatusPanel(this, SWT.NONE, injector);
		FormData fd_statusPanel = new FormData();
		fd_statusPanel.right = new FormAttachment(100, -12);
		fd_statusPanel.top = new FormAttachment(0, statusPanelTop);
		fd_statusPanel.bottom = new FormAttachment(0, statusPanelTop+statusPanelHeight);
		fd_statusPanel.left = new FormAttachment(0, 12);
		statusPanel.setLayoutData(fd_statusPanel);
		
		FormData fd_mainStackView = new FormData();
		fd_mainStackView.left = new FormAttachment(0, 12);
		fd_mainStackView.right = new FormAttachment(100, -12);
		fd_mainStackView.bottom = new FormAttachment(100, -12);
		fd_mainStackView.top = new FormAttachment(statusPanel, 6);
		mainStackView.setLayoutData(fd_mainStackView);
		
	}
	
	public MainStackView getMainStackView() {
		return mainStackView;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
