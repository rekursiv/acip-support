package org.asianclassics.center.catalog.entry.stamp;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.asianclassics.center.catalog.entry.model.StampModel;
import org.asianclassics.center.catalog.entry.model.StampRepo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.ektorp.AttachmentInputStream;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class StampScanDialog extends Dialog {

	private static final int stampMaxSize = 150;
	private Shell shell;
	private Label lblScanHowTo;
	private Button btnImport;
	private Button btnCancel;
	private int stampNum;
	private StampRepo repo;
	private Logger log;


	public StampScanDialog(Shell parent, Injector injector) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setText("Scan New Stamp");
		if (injector!=null) injector.injectMembers(this);
	}
	
	@Inject
	public void inject(StampRepo sr, Logger log) {
		repo = sr;
		this.log = log;
	}

	public int open() {
		stampNum = 0;
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return stampNum;
	}

	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 200);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		lblScanHowTo = new Label(shell, SWT.NONE);
		FormData fd_lblScanHowTo = new FormData();
		fd_lblScanHowTo.right = new FormAttachment(100, -12);
		fd_lblScanHowTo.bottom = new FormAttachment(0, 115);
		fd_lblScanHowTo.top = new FormAttachment(0, 10);
		fd_lblScanHowTo.left = new FormAttachment(0, 10);
		lblScanHowTo.setLayoutData(fd_lblScanHowTo);
		lblScanHowTo.setText("New Label");
		
		btnImport = new Button(shell, SWT.NONE);
		btnImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				importStamp();
				if (stampNum!=0) shell.close();
			}
		});
		FormData fd_btnImport = new FormData();
		fd_btnImport.right = new FormAttachment(0, 109);
		fd_btnImport.top = new FormAttachment(0, 125);
		fd_btnImport.left = new FormAttachment(0, 25);
		btnImport.setLayoutData(fd_btnImport);
		btnImport.setText("Import Stamp");
		
		btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				stampNum = 0;
				shell.close();
			}
		});
		
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(btnImport, 0, SWT.TOP);
		fd_btnCancel.left = new FormAttachment(btnImport, 50);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");

		StringBuilder sb = new StringBuilder();
		sb.append("Please scan, crop, and save the stamp now.\n");
		sb.append("Save your scan as \"Scanned Document.jpg\" in your \"Documents\" folder.\n");
		sb.append("(This is the default in Simple Scan.)");
		lblScanHowTo.setText(sb.toString());
	}
	
	private void showMessage(String msg) {
		MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL|SWT.OK);
        messageBox.setText("Error Importing Stamp");
        messageBox.setMessage(msg);
        messageBox.open();
	}
	
	private void importStamp() {
		stampNum = repo.getLatestStampIndex()+1;	
		log.info("next stamp # "+stampNum);

		StringBuilder pathName = new StringBuilder();
		pathName.append("c:/scratch/temp/new_stamps/test.jpg");   ////  TEST
//		pathName.append(System.getProperty("user.home"));
//		pathName.append("/Documents");
//		pathName.append("/Scanned Document.jpg");
		log.info(pathName.toString());
		
		Path path = Paths.get(pathName.toString());
		String category = "New";  //  TODO:  category chooser
		
		InputStream is = null;
		try {
			is = Files.newInputStream(path);
		} catch (IOException e) {
			showMessage("\"Scanned Document.jpg\" was not found in \"Documents\" folder.");
			stampNum=0;
			return;
		}
		
		ByteArrayOutputStream stampBuffer = null;
		try {
			stampBuffer = scaleStamp(is);
		} catch (Exception e) {
			showMessage("Error scaling stamp:  "+e.getMessage());
			stampNum=0;
			return;
		}
		

		StampModel stamp = new StampModel();
		stamp.index = stampNum;
		stamp.category = category;
		
		try {
			addStampToDb(stamp, new ByteArrayInputStream(stampBuffer.toByteArray()));
		} catch (Exception e) {
			showMessage("Error writing stamp to databse:  "+e.getMessage());
			stampNum=0;
			return;
		}
		
		try {
			is.close();
			Files.delete(path);
		} catch (IOException e) {
			log.log(Level.SEVERE, "", e);
		}
		
	}
	
	private ByteArrayOutputStream scaleStamp(InputStream is) throws Exception {
		ByteArrayOutputStream stampBuffer = new ByteArrayOutputStream();
		BufferedImage stamp = ImageIO.read(is);
		int width = stamp.getWidth();
		int height = stamp.getHeight();
		Image scaledStamp = null;
		if (width>height) {
			height = (int)(((float)stampMaxSize/(float)width)*height);
			width = stampMaxSize;
			scaledStamp = stamp.getScaledInstance(width, -1, Image.SCALE_AREA_AVERAGING);
		}
		else {
			width = (int)(((float)stampMaxSize/(float)height)*width);
			height = stampMaxSize;
			scaledStamp = stamp.getScaledInstance(-1, height, Image.SCALE_AREA_AVERAGING);
		}
		BufferedImage bufferedStamp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		bufferedStamp.getGraphics().drawImage(scaledStamp, 0, 0, null);
		ImageIO.write(bufferedStamp, "jpg", stampBuffer);
//		ImageIO.write(bufferedStamp, "jpg", new File("c:/scratch/temp/new_stamps/out.jpg"));   ///  TEST
		return stampBuffer;
	}

	private void addStampToDb(StampModel stamp, InputStream is) throws Exception {
		repo.add(stamp);
		AttachmentInputStream ais = new AttachmentInputStream("stamp.jpg", is, "image/jpeg");
		repo.getDb().createAttachment(stamp.getId(), stamp.getRevision(), ais);
	}
	
	
}
