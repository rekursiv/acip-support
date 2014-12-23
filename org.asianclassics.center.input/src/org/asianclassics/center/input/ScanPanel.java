package org.asianclassics.center.input;

import java.util.logging.Logger;

import org.asianclassics.text.edit.AcipEditorCaretMoveEvent;
import org.asianclassics.text.edit.AcipEditorScrollEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;


public class ScanPanel extends ScrolledComposite {
	
	private Image scan = null;
	private Label lblImage;
	private Logger log;
	
	public ScanPanel(Composite parent, Injector injector) {

		super(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		setLayout(new FillLayout(SWT.HORIZONTAL));

		setExpandHorizontal(true);
		setExpandVertical(true);

		lblImage = new Label(this, SWT.NONE);
		lblImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent arg0) {
				test();
			}
		});
		setContent(lblImage);
		
		if (injector!=null) injector.injectMembers(this);
	}

	protected void test() {
		this.setOrigin(100, 100);
	}

	@Inject
	public void inject(Logger logger) {
		this.log = logger;
	}
	
	
	@Subscribe
	public void onEditorScroll(AcipEditorScrollEvent evt) {
//		log.info("");
		setOrigin(evt.getX(), evt.getY());
	}
	
	@Subscribe
	public void onEditorCaretMove(AcipEditorCaretMoveEvent evt) {
//		log.info(""+evt.getX());
		setOrigin(evt.getX(), evt.getY());
	}
	
	@Subscribe
	public void onScale(ScanScaleEvent evt) {
		setImageScale((float)evt.getScale()/100.0f);
		setMinSize(lblImage.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void setImage(ImageData imgData) {
		if (imgData==null) scan = null;
		else scan = new Image(Display.getDefault(), imgData);
		setImageScale(1);
		setMinSize(lblImage.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	
	private void setImageScale(float scale) {
		if (scan==null) {
			lblImage.setImage(null);
		} else {
			int width = (int)((float)scan.getImageData().width*scale);
			int height = (int)((float)scan.getImageData().height*scale);
			lblImage.setImage(resizeImage(scan, width, height));
		}
	}
	
	
	private void setImageSize(float size) {
		if (scan==null) {
			lblImage.setImage(null);
		} else {
			float scale = size / (float)scan.getImageData().width;
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
}
