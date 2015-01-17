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

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;

public class ScanInputPairPanel extends Composite {
	private AcipEditor editor;
	private ScanPanel pnlScan;

	protected Button btnSave;
	protected Scale scale;
	protected Label lblZoom;
	protected Label lblZoomTitle;
	private Logger log;

	
	public ScanInputPairPanel(Composite parent, Injector injector) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		SashForm sashForm = new SashForm(this, SWT.VERTICAL);
		pnlScan = new ScanPanel(sashForm, injector);
		
		editor = new AcipEditor(sashForm);
		sashForm.setWeights(new int[] {7, 3});
		
		if (injector!=null) injector.injectMembers(this);
	}


	@Inject
	public void inject(Logger log, EventBus eb, InputTaskController itCon) {
		this.log = log;
		editor.setEventBus(eb);
		itCon.setEditor(editor);
		itCon.setScan(pnlScan);
	}

/*	
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
*/
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
