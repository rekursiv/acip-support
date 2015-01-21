package org.asianclassics.center.input;

import java.util.logging.Logger;

import org.asianclassics.center.TaskView;
import org.asianclassics.center.event.LogoutEvent;
import org.asianclassics.text.edit.AcipEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class InputControlPanel extends Composite {
	private EventBus eb;
	private Logger log;

	private Button btnLogout;
	private Button btnFinish;
	
	protected Scale scale;
	protected Label lblZoom;
	protected Label lblZoomTitle;
	private Button btnAutoAlignScan;
	private InputTaskController itCon;
	

	
	public InputControlPanel(Composite parent, Injector injector) {
		super(parent, SWT.NONE);
		setLayout(new FormLayout());
		
		btnLogout = new Button(this, SWT.NONE);
		btnLogout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				eb.post(new LogoutEvent());
			}
		});
		FormData fd_btnLogout = new FormData();
		fd_btnLogout.right = new FormAttachment(0, 85);
		fd_btnLogout.bottom = new FormAttachment(0, 35);
		fd_btnLogout.top = new FormAttachment(0, 10);
		fd_btnLogout.left = new FormAttachment(0, 15);
		btnLogout.setLayoutData(fd_btnLogout);
		btnLogout.setText("Logout");
		
		btnFinish = new Button(this, SWT.NONE);
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				itCon.finishTask();
			}
		});
		FormData fd_btnFinish = new FormData();
		fd_btnFinish.bottom = new FormAttachment(btnLogout, 25);
		fd_btnFinish.top = new FormAttachment(btnLogout, 0, SWT.TOP);
		fd_btnFinish.right = new FormAttachment(0, 165);
		fd_btnFinish.left = new FormAttachment(0, 102);
		btnFinish.setLayoutData(fd_btnFinish);
		btnFinish.setText("Finish");
		
		scale = new Scale(this, SWT.NONE);
		scale.setMaximum(200);
		scale.setMinimum(20);
		scale.setSelection(100);
		scale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				onZoom();
			}
		});
		FormData fd_scale = new FormData();
		fd_scale.top = new FormAttachment(0, -2);
		fd_scale.bottom = new FormAttachment(0, 40);
		fd_scale.right = new FormAttachment(0, 850);
		fd_scale.left = new FormAttachment(0, 550);
		scale.setLayoutData(fd_scale);
		
		lblZoom = new Label(this, SWT.NONE);
		FormData fd_lblZoom = new FormData();
		fd_lblZoom.right = new FormAttachment(0, 905);
		fd_lblZoom.left = new FormAttachment(scale, 6);
		lblZoom.setLayoutData(fd_lblZoom);
		lblZoom.setText("100%");
		
		lblZoomTitle = new Label(this, SWT.NONE);
		fd_lblZoom.bottom = new FormAttachment(lblZoomTitle, 0, SWT.BOTTOM);
		FormData fd_lblZoomTitle = new FormData();
		fd_lblZoomTitle.left = new FormAttachment(0, 485);
		fd_lblZoomTitle.right = new FormAttachment(scale);
		lblZoomTitle.setLayoutData(fd_lblZoomTitle);
		lblZoomTitle.setText("Zoom");
		
		btnAutoAlignScan = new Button(this, SWT.CHECK);
		fd_lblZoomTitle.bottom = new FormAttachment(btnAutoAlignScan, 0, SWT.BOTTOM);
		btnAutoAlignScan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent evt) {
				eb.post(new ScanAlignModeEvent(btnAutoAlignScan.getSelection()));
			}
		});
		btnAutoAlignScan.setSelection(true);
		FormData fd_btnAutoAlignScan = new FormData();
		fd_btnAutoAlignScan.bottom = new FormAttachment(btnFinish, 16);
		fd_btnAutoAlignScan.top = new FormAttachment(btnFinish, 0, SWT.TOP);
		fd_btnAutoAlignScan.right = new FormAttachment(0, 435);
		fd_btnAutoAlignScan.left = new FormAttachment(0, 275);
		btnAutoAlignScan.setLayoutData(fd_btnAutoAlignScan);
		btnAutoAlignScan.setText("Auto-Align Scan");
	

		
		if (injector!=null) injector.injectMembers(this);
	}

	@Inject
	public void inject(Logger log, EventBus eb, InputTaskController itCon) {
		this.log = log;
		this.eb = eb;
		this.itCon = itCon;
	}

	protected void onZoom() {
		lblZoom.setText(scale.getSelection()+"%");
//		log.info(""+scale.getSelection());
		eb.post(new ScanScaleEvent(scale.getSelection()));
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
