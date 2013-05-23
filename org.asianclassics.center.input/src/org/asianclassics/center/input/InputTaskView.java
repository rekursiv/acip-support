package org.asianclassics.center.input;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import org.asianclassics.center.TaskView;
import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.LogoutEvent;
import org.asianclassics.center.event.StatusPanelUpdateEvent;
import org.asianclassics.tibetan.edit.TibetanEditor;
import org.asianclassics.tibetan.transcoder.TibetanTranscoder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;

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
	private TibetanTranscoder xcdr = null;
	private Label lblImage;
	private InputTaskController itCon;
	private Label lblStatus;
	private Text txtWorkingTibetan;
	private Text txtWorkingRoman;
	private Logger log;
	private Label lblStatic1;
	private Label lblReferenceText;
	private Text txtRefRoman;
	private Text txtRefTibetan;
	private Image scan = null;

	
	public InputTaskView(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(new FormLayout());
		
		lblStatic1 = new Label(this, SWT.NONE);
		FormData fd_lblStatic1 = new FormData();
		lblStatic1.setLayoutData(fd_lblStatic1);
		lblStatic1.setText("Working Text");
		
		txtWorkingRoman = new Text(this, SWT.BORDER);
		fd_lblStatic1.bottom = new FormAttachment(100, -182);
		fd_lblStatic1.top = new FormAttachment(100, -203);
		FormData fd_txtWorkingRoman = new FormData();
		fd_txtWorkingRoman.bottom = new FormAttachment(100, -150);
		fd_txtWorkingRoman.top = new FormAttachment(100, -171);
		txtWorkingRoman.setLayoutData(fd_txtWorkingRoman);
		
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
				String roman = xcdr.transcode(editor.getWorkingText(), TibetanTranscoder.TIBETAN);
				itCon.finishTask(roman);
			}
		});
		FormData fd_btnFinish = new FormData();
		fd_btnFinish.top = new FormAttachment(btnLogout, 0, SWT.TOP);
		fd_btnFinish.left = new FormAttachment(0, 150);
		btnFinish.setLayoutData(fd_btnFinish);
		btnFinish.setText("Finish");
		
		editor = new TibetanEditor(this, SWT.NONE);
		fd_lblStatic1.right = new FormAttachment(editor, 70);
		fd_lblStatic1.left = new FormAttachment(editor, 0, SWT.LEFT);
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(100, -12);
		fd_composite.left = new FormAttachment(0, 12);
		editor.setLayoutData(fd_composite);
		
		lblImage = new Label(this, SWT.BORDER);
		fd_composite.top = new FormAttachment(0, 320);
		fd_composite.bottom = new FormAttachment(100, -213);
		FormData fd_lblImage = new FormData();
		fd_lblImage.bottom = new FormAttachment(0, 305);
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
		
		txtWorkingTibetan = new Text(this, SWT.BORDER);
		fd_txtWorkingRoman.right = new FormAttachment(100, -23);
		fd_txtWorkingRoman.left = new FormAttachment(txtWorkingTibetan, 0, SWT.LEFT);
		txtWorkingTibetan.setFont(SWTResourceManager.getFont("Segoe UI", 26, SWT.NORMAL));
		FormData fd_txtWorkingTibetan = new FormData();
		fd_txtWorkingTibetan.right = new FormAttachment(100, -23);
		fd_txtWorkingTibetan.left = new FormAttachment(btnLogout, 0, SWT.LEFT);
		fd_txtWorkingTibetan.bottom = new FormAttachment(100, -105);
		fd_txtWorkingTibetan.top = new FormAttachment(100, -158);
		txtWorkingTibetan.setLayoutData(fd_txtWorkingTibetan);
		
		lblReferenceText = new Label(this, SWT.NONE);
		lblReferenceText.setText("Reference Text");
		FormData fd_lblReferenceText = new FormData();
		fd_lblReferenceText.left = new FormAttachment(lblStatic1, 0, SWT.LEFT);
		lblReferenceText.setLayoutData(fd_lblReferenceText);
		
		txtRefRoman = new Text(this, SWT.BORDER);
		fd_lblReferenceText.top = new FormAttachment(txtRefRoman, -21, SWT.TOP);
		fd_lblReferenceText.bottom = new FormAttachment(txtRefRoman, -6);
		FormData fd_txtRefRoman = new FormData();
		fd_txtRefRoman.bottom = new FormAttachment(100, -58);
		fd_txtRefRoman.top = new FormAttachment(100, -79);
		txtRefRoman.setLayoutData(fd_txtRefRoman);
		
		txtRefTibetan = new Text(this, SWT.BORDER);
		fd_txtRefRoman.right = new FormAttachment(100, -23);
		fd_txtRefRoman.left = new FormAttachment(txtRefTibetan, 0, SWT.LEFT);
		txtRefTibetan.setFont(SWTResourceManager.getFont("Segoe UI", 26, SWT.NORMAL));
		FormData fd_txtRefTibetan = new FormData();
		fd_txtRefTibetan.right = new FormAttachment(100, -23);
		fd_txtRefTibetan.left = new FormAttachment(txtWorkingTibetan, 0, SWT.LEFT);
		fd_txtRefTibetan.bottom = new FormAttachment(100, -12);
		fd_txtRefTibetan.top = new FormAttachment(100, -65);
		txtRefTibetan.setLayoutData(fd_txtRefTibetan);
		
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
	
		xcdr = new TibetanTranscoder();
		editor.init(xcdr);
		editor.getSourceViewer().getTextWidget().addCaretListener(new CaretListener() {
			@Override
			public void caretMoved(CaretEvent arg0) {
				updateDebugView();
			}			
		});
		
	}

	
	public void setWorkingText(String text) {
		String tib = xcdr.transcode(text, TibetanTranscoder.ROMAN);
		editor.setWorkingText(tib);
		updateDebugView();
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
		String tib = null;
		if (text==null) {
			txtRefRoman.setText("");
			txtRefTibetan.setText("");
		} else {
			tib = xcdr.transcode(text, TibetanTranscoder.ROMAN);
			String roman = xcdr.transcode(text, TibetanTranscoder.TIBETAN);
			txtRefRoman.setText(roman);
			txtRefTibetan.setText(tib);
		}
		editor.setReferenceText(tib);
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
	public void updateDebugView() {
//		log.info("*");
		String text = editor.getWorkingText();
		String tib = xcdr.transcode(text, TibetanTranscoder.ROMAN);
		String roman = xcdr.transcode(text, TibetanTranscoder.TIBETAN);
		txtWorkingRoman.setText(roman);
		txtWorkingTibetan.setText(tib);
	}
	
}
