package org.asianclassics.center.catalog.entry.stamp;

import java.util.logging.Logger;

import org.asianclassics.center.catalog.entry.model.StampRepo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class StampSelectDialog extends Dialog {

	protected int stampNum;
	protected Shell shell;
	private Logger log;
	private StampRepo repo;
	private Label lblImage;
	private Label lblEnterAStamp;
	private Text txtStampNum;
	private Button btnBrowseStamps;
	private Button btnScanNewStamp;
	private Button btnAccept;
	private Button btnCancel;
	private StampBrowseDialog browser;
	private StampScanDialog scanner;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public StampSelectDialog(Shell parent, Injector injector) {
		super(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setText("Stamp Selection");
		browser = new StampBrowseDialog(parent, injector);
		scanner = new StampScanDialog(parent, injector);
		if (injector!=null) injector.injectMembers(this);
	}
	
	@Inject
	public void inject(StampRepo sr, Logger log) {
		repo = sr;
		this.log = log;
	}

	/**
	 * Open the dialog.
	 * @return the selected stamp index
	 */
	public int open() {
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

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(400, 300);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		lblImage = new Label(shell, SWT.NONE);
		FormData fd_lblImage = new FormData();
		fd_lblImage.bottom = new FormAttachment(0, 195);
		fd_lblImage.right = new FormAttachment(0, 175);
		fd_lblImage.top = new FormAttachment(0, 12);
		fd_lblImage.left = new FormAttachment(0, 12);
		lblImage.setLayoutData(fd_lblImage);
//		lblImage.setText("Image");
		
		lblEnterAStamp = new Label(shell, SWT.NONE);
		FormData fd_lblEnterAStamp = new FormData();
		fd_lblEnterAStamp.top = new FormAttachment(0, 20);
		fd_lblEnterAStamp.left = new FormAttachment(0, 200);
		lblEnterAStamp.setLayoutData(fd_lblEnterAStamp);
		lblEnterAStamp.setText("Enter stamp number:");
		
		txtStampNum = new Text(shell, SWT.BORDER);
		txtStampNum.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				onModifyText();
			}
		});
		FormData fd_txtStampNum = new FormData();
		fd_txtStampNum.top = new FormAttachment(lblEnterAStamp, 6);
		fd_txtStampNum.left = new FormAttachment(lblEnterAStamp, 0, SWT.LEFT);
		txtStampNum.setLayoutData(fd_txtStampNum);
		
		btnBrowseStamps = new Button(shell, SWT.NONE);
		btnBrowseStamps.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				stampNum = browser.open();
				txtStampNum.setText(String.valueOf(stampNum));
			}
		});
		FormData fd_btnBrowseStamps = new FormData();
		fd_btnBrowseStamps.top = new FormAttachment(txtStampNum, 6);
		fd_btnBrowseStamps.left = new FormAttachment(txtStampNum, 0, SWT.LEFT);
		btnBrowseStamps.setLayoutData(fd_btnBrowseStamps);
		btnBrowseStamps.setText("Browse Stamps");
		
		btnAccept = new Button(shell, SWT.NONE);
		btnAccept.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
//				stampNum = Integer.parseInt(txtStampNum.getText());
				shell.close();
			}
		});
		FormData fd_btnAccept = new FormData();
		fd_btnAccept.top = new FormAttachment(0, 205);
		fd_btnAccept.left = new FormAttachment(0, 210);
		btnAccept.setLayoutData(fd_btnAccept);
		btnAccept.setText("Accept");
		
		btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				stampNum = 0;  //  -1 ??
				shell.close();
			}
		});
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(btnAccept, 0, SWT.TOP);
		fd_btnCancel.left = new FormAttachment(btnAccept, 6);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		
		btnScanNewStamp = new Button(shell, SWT.NONE);
		btnScanNewStamp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				stampNum = scanner.open();
				txtStampNum.setText(String.valueOf(stampNum));
			}
		});
		FormData fd_btnScanNewStamp = new FormData();
		fd_btnScanNewStamp.top = new FormAttachment(btnBrowseStamps, 6);
		fd_btnScanNewStamp.left = new FormAttachment(btnBrowseStamps, 0, SWT.LEFT);
		btnScanNewStamp.setLayoutData(fd_btnScanNewStamp);
		btnScanNewStamp.setText("Scan New Stamp");

	}
	
	private void onModifyText() {
		try {
			stampNum = Integer.parseInt(txtStampNum.getText());
			loadImage();
		} catch (NumberFormatException e) {
			invalidate();
		}
	}

	private void loadImage() {
		try {
			ImageData imgData = repo.getImageByIndex(stampNum);
			Image img = new Image(shell.getDisplay(), imgData);
			lblImage.setImage(img);
		} catch (Exception e) {
			invalidate();
		}
	}
	
	private void invalidate() {
		stampNum = 0;
		lblImage.setImage(null);
		lblImage.setText("Invalid Stamp Number");
	}
	
	private void test() {
		log.info("r="+stampNum);
	}
}
