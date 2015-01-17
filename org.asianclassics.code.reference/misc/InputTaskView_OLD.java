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

public class InputTaskView_OLD extends TaskView {
	private Button btnLogout;
	private EventBus eb;
	private Button btnFinish;
	private AcipEditor editor;
	private ScanPanel pnlScan;
	private InputTaskController itCon;
	private Logger log;

	protected Button btnSave;
	protected Scale scale;
	protected Label lblZoom;
	protected Label lblZoomTitle;
	private Button btnAutoAlignScan;

	
	public InputTaskView_OLD(Composite parent, Injector injector) {
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
		fd_btnLogout.right = new FormAttachment(0, 65);
		fd_btnLogout.bottom = new FormAttachment(0, 35);
		fd_btnLogout.top = new FormAttachment(0, 10);
		fd_btnLogout.left = new FormAttachment(0, 15);
		btnLogout.setLayoutData(fd_btnLogout);
		btnLogout.setText("Logout");
		
		btnFinish = new Button(this, SWT.NONE);
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				itCon.finishTask(editor.getWorkingText());
			}
		});
		FormData fd_btnFinish = new FormData();
		fd_btnFinish.top = new FormAttachment(btnLogout, -25);
		fd_btnFinish.bottom = new FormAttachment(btnLogout, 0, SWT.BOTTOM);
		fd_btnFinish.right = new FormAttachment(0, 120);
		fd_btnFinish.left = new FormAttachment(0, 77);
		btnFinish.setLayoutData(fd_btnFinish);
		btnFinish.setText("Finish");
		
		editor = new AcipEditor(this);
		FormData fd_composite = new FormData();
		editor.setLayoutData(fd_composite);
		

		
		pnlScan = new ScanPanel(this, injector);
		fd_composite.left = new FormAttachment(pnlScan, 0, SWT.LEFT);
		fd_composite.right = new FormAttachment(pnlScan, 0, SWT.RIGHT);
		fd_composite.top = new FormAttachment(100, -271);
		fd_composite.bottom = new FormAttachment(100, -12);
		FormData fd_lblImage = new FormData();
		fd_lblImage.bottom = new FormAttachment(editor);
		fd_lblImage.left = new FormAttachment(0, 12);
		fd_lblImage.right = new FormAttachment(100, -12);
		fd_lblImage.top = new FormAttachment(0, 50);
		pnlScan.setLayoutData(fd_lblImage);
		
		btnSave = new Button(this, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.save(editor.getWorkingText());
			}
		});
		FormData fd_btnSave = new FormData();
		fd_btnSave.top = new FormAttachment(btnFinish, -25);
		fd_btnSave.bottom = new FormAttachment(btnFinish, 0, SWT.BOTTOM);
		fd_btnSave.right = new FormAttachment(0, 286);
		fd_btnSave.left = new FormAttachment(0, 250);
		btnSave.setLayoutData(fd_btnSave);
		btnSave.setText("Save");
		
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
		fd_scale.right = new FormAttachment(0, 950);
		fd_scale.left = new FormAttachment(0, 645);
		scale.setLayoutData(fd_scale);
		
		lblZoom = new Label(this, SWT.NONE);
		FormData fd_lblZoom = new FormData();
		fd_lblZoom.right = new FormAttachment(scale, 34, SWT.RIGHT);
		fd_lblZoom.left = new FormAttachment(scale, 6);
		lblZoom.setLayoutData(fd_lblZoom);
		lblZoom.setText("100%");
		
		lblZoomTitle = new Label(this, SWT.NONE);
		fd_lblZoom.bottom = new FormAttachment(lblZoomTitle, 0, SWT.BOTTOM);
		FormData fd_lblZoomTitle = new FormData();
		fd_lblZoomTitle.left = new FormAttachment(scale, -32, SWT.LEFT);
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
		fd_btnAutoAlignScan.top = new FormAttachment(btnSave, 0, SWT.TOP);
		fd_btnAutoAlignScan.right = new FormAttachment(0, 513);
		fd_btnAutoAlignScan.left = new FormAttachment(0, 405);
		btnAutoAlignScan.setLayoutData(fd_btnAutoAlignScan);
		btnAutoAlignScan.setText("Auto-Align Scan");
		
		Button btnType = new Button(this, SWT.NONE);
		btnType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.testInput();
			}
		});
		FormData fd_btnType = new FormData();
		fd_btnType.top = new FormAttachment(btnFinish, 0, SWT.TOP);
		fd_btnType.left = new FormAttachment(btnFinish, 6);
		btnType.setLayoutData(fd_btnType);
		btnType.setText("Type");
		
		if (injector!=null) injector.injectMembers(this);
	}

	protected void onZoom() {
		lblZoom.setText(scale.getSelection()+"%");
//		log.info(""+scale.getSelection());
		eb.post(new ScanScaleEvent(scale.getSelection()));
	}

	@Inject
	public void inject(Logger log, EventBus eb, InputTaskController itCon) {
		this.log = log;
		this.eb = eb;
		this.itCon = itCon;
//		itCon.setView(this);
		editor.setEventBus(eb);

/*
		pnlScan.addListener(SWT.Resize,  new Listener () {
			    public void handleEvent (Event e) {
			    	onResize();
			    }
		});
	*/
	}

	
	public void setWorkingText(String text) {
		editor.setWorkingText(text);
	}
	
	@Override
	public boolean setFocus() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				editor.setFocus();
			}
		});
		return true;
	}
	
	public void setReferenceText(String text) {
		editor.setReferenceText(text);
	}
	

	public void setImage(ImageData imgData) {
		pnlScan.setImage(imgData);
	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
