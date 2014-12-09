package org.asianclassics.center.input;

import java.util.logging.Logger;

import org.asianclassics.center.TaskView;
import org.asianclassics.center.event.LogoutEvent;
import org.asianclassics.text.edit.AcipEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class InputTaskView extends TaskView {
	private Button btnLogout;
	private EventBus eb;
	private Button btnFinish;
	private AcipEditor editor;
	private Label lblImage;
	private InputTaskController itCon;
	private Label lblStatus;
	private Logger log;
	
	private Label lblReferenceText;
	private Text txtReference;
	private Image scan = null;

	
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
		fd_btnFinish.left = new FormAttachment(0, 150);
		btnFinish.setLayoutData(fd_btnFinish);
		btnFinish.setText("Finish");
		
		editor = new AcipEditor(this);
		FormData fd_composite = new FormData();
		editor.setLayoutData(fd_composite);
		
		lblImage = new Label(this, SWT.BORDER);
		fd_composite.right = new FormAttachment(100, -12);
		fd_composite.left = new FormAttachment(0, 12);
		fd_composite.top = new FormAttachment(0, 210);
		fd_composite.bottom = new FormAttachment(0, 310);
		FormData fd_lblImage = new FormData();
		fd_lblImage.bottom = new FormAttachment(0, 185);
		fd_lblImage.left = new FormAttachment(0, 12);
		fd_lblImage.right = new FormAttachment(100, -12);
		fd_lblImage.top = new FormAttachment(0, 85);
		lblImage.setLayoutData(fd_lblImage);
		
		lblStatus = new Label(this, SWT.NONE);
		FormData fd_lblStatus = new FormData();
		fd_lblStatus.right = new FormAttachment(0, 530);
		fd_lblStatus.top = new FormAttachment(0, 50);
		fd_lblStatus.left = new FormAttachment(lblImage, 0, SWT.LEFT);
		lblStatus.setLayoutData(fd_lblStatus);
		lblStatus.setText("lblStatus");
		
		lblReferenceText = new Label(this, SWT.NONE);
		lblReferenceText.setText("Reference Text");
		FormData fd_lblReferenceText = new FormData();
		fd_lblReferenceText.right = new FormAttachment(0, 90);
		fd_lblReferenceText.left = new FormAttachment(0, 13);
		lblReferenceText.setLayoutData(fd_lblReferenceText);
		
		txtReference = new Text(this, SWT.BORDER);
		fd_lblReferenceText.top = new FormAttachment(0, 335);
		fd_lblReferenceText.bottom = new FormAttachment(0, 350);
		txtReference.setFont(SWTResourceManager.getFont("Segoe UI", 26, SWT.NORMAL));
		FormData fd_txtRefTibetan = new FormData();
		fd_txtRefTibetan.right = new FormAttachment(100, -15);
		fd_txtRefTibetan.left = new FormAttachment(0, 12);
		fd_txtRefTibetan.bottom = new FormAttachment(0, 420);
		fd_txtRefTibetan.top = new FormAttachment(0, 367);
		txtReference.setLayoutData(fd_txtRefTibetan);
		
		if (injector!=null) injector.injectMembers(this);
	}
	
	@Inject
	public void inject(Logger logger, EventBus eb, InputTaskController itCon) {
		this.log = logger;
		this.eb = eb;
		this.itCon = itCon;
		itCon.setView(this);

		lblImage.addListener(SWT.Resize,  new Listener () {
			    public void handleEvent (Event e) {
			    	onResize();
			    }
		});
	
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
		
							//  DEBUG
		if (text==null) txtReference.setText("null");
		else txtReference.setText(text);
	}
	
	public void setImage(ImageData imgData) {
		if (imgData==null) scan = null;
		else scan = new Image(Display.getDefault(), imgData);
		onResize();
	}
	

	public void setStatus(String msg) {
		lblStatus.setText(msg);
	}
	
	private void onResize() {
		if (scan==null) {
			lblImage.setImage(null);
		} else {
			float scale = (float)lblImage.getSize().x / (float)scan.getImageData().width;
			int width = (int)((float)scan.getImageData().width*scale);
			int height = (int)((float)scan.getImageData().height*scale);
			lblImage.setImage(resizeImage(scan, width, height));
		}
	}
	
	private Image resizeImage(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, 
		image.getBounds().width, image.getBounds().height, 
		0, 0, width, height);
		gc.dispose();
		return scaled;
	}
	
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	
	
	/////////   DEBUG
//	public void updateDebugView() {
//		log.info("*");
//		String text = editor.getWorkingText();
//		String tib = xcdr.transcode(text, TibetanTranscoder.ROMAN);
//		String roman = xcdr.transcode(text, TibetanTranscoder.TIBETAN);
//		txtWorkingRoman.setText("roman");
//		txtWorking.setText("tib");
//	}
	
}
