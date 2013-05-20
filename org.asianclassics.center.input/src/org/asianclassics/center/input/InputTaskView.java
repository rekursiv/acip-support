package org.asianclassics.center.input;

import org.asianclassics.center.TaskView;
import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.LogoutEvent;
import org.asianclassics.tibetan.edit.TibetanEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.wb.swt.SWTResourceManager;

public class InputTaskView extends TaskView {
	private Button btnLogout;
	private EventBus eb;
	private Button btnFinish;
	private TibetanEditor editor;
	private Label lblImage;
	private InputTaskController itCon;

	
	public InputTaskView(Composite parent, int style, Injector injector) {
		super(parent, style);
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
		fd_btnFinish.top = new FormAttachment(btnLogout, 0, SWT.TOP);
		fd_btnFinish.left = new FormAttachment(20);
		btnFinish.setLayoutData(fd_btnFinish);
		btnFinish.setText("Finish");
		
		editor = new TibetanEditor(this, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(100, -12);
		fd_composite.left = new FormAttachment(0, 12);
		editor.setLayoutData(fd_composite);
		
		lblImage = new Label(this, SWT.BORDER);
		fd_composite.top = new FormAttachment(0, 320);
		fd_composite.bottom = new FormAttachment(100, -12);
		FormData fd_lblImage = new FormData();
		fd_lblImage.bottom = new FormAttachment(0, 305);
		fd_lblImage.left = new FormAttachment(0, 12);
		fd_lblImage.right = new FormAttachment(100, -12);
		fd_lblImage.top = new FormAttachment(0, 85);
		lblImage.setLayoutData(fd_lblImage);
		
		if (injector!=null) injector.injectMembers(this);
	}
	
	@Inject
	public void inject(EventBus eb, InputTaskController itCon) {
		this.eb = eb;
		this.itCon = itCon;
		itCon.setView(this);
		editor.init(null);
	}

	
	public void setWorkingText(String text) {
		editor.setWorkingText(text);
		editor.setFocus();  //  i focus u, y u no work??
	}
	
	public void setReferenceText(String text) {
		editor.setReferenceText(text);
	}
	
	public void setImage(ImageData imgData) {
		Image img = new Image(Display.getDefault(), imgData);
		lblImage.setImage(img);
//		lblImage.pack();
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
