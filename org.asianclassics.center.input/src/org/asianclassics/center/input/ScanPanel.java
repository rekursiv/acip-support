package org.asianclassics.center.input;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.google.inject.Injector;
import org.eclipse.swt.layout.FillLayout;


public class ScanPanel extends ScrolledComposite {
	
	private Image scan = null;
	private Label lblImage;
	
	public ScanPanel(Composite parent, Injector injector) {

		super(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		setLayout(new FillLayout(SWT.HORIZONTAL));

		setExpandHorizontal(true);
		setExpandVertical(true);

		lblImage = new Label(this, SWT.NONE);
		setContent(lblImage);

		
	}
	
	
	public void setImage(ImageData imgData) {
		if (imgData==null) scan = null;
		else scan = new Image(Display.getDefault(), imgData);

		setImageSize(3000);
		setMinSize(lblImage.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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
