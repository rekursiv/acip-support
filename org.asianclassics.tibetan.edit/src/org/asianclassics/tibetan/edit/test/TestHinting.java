package org.asianclassics.tibetan.edit.test;

import org.asianclassics.tibetan.edit.TibetanEditor;
import org.asianclassics.tibetan.transcoder.TibetanTranscoder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;


public class TestHinting extends Composite {

	private TibetanEditor hintEd;
	
	private Text txtRef;
	private Text txtPos;
	private Text txtHint;

	private Button btnDump;
	

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public TestHinting(Composite parent, int style) {
		super(parent, style);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
//				tibEdit.test();
			}
		});
		setLayout(new FormLayout());


		
		txtRef = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
		FormData fd_txtRef = new FormData();
		fd_txtRef.left = new FormAttachment(0, 10);
		fd_txtRef.right = new FormAttachment(100, -10);
		fd_txtRef.top = new FormAttachment(0, 10);
		fd_txtRef.bottom = new FormAttachment(0, 110);
		txtRef.setLayoutData(fd_txtRef);
		
		Button btnCopy = new Button(this, SWT.NONE);
		btnCopy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				hintEd.setWorkingText(txtRef.getText());
			}
		});
		FormData fd_btnCopy = new FormData();
		fd_btnCopy.left = new FormAttachment(0, 10);
		fd_btnCopy.bottom = new FormAttachment(100, -10);
		btnCopy.setLayoutData(fd_btnCopy);
		btnCopy.setText("Copy");
		
		txtPos = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtPos = new FormData();
		fd_txtPos.top = new FormAttachment(0, 222);
		fd_txtPos.left = new FormAttachment(0, 10);
		fd_txtPos.right = new FormAttachment(0, 106);
		txtPos.setLayoutData(fd_txtPos);
		
		txtHint = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_txtHint = new FormData();
		fd_txtHint.top = new FormAttachment(0, 222);
		fd_txtHint.left = new FormAttachment(0, 115);
		fd_txtHint.right = new FormAttachment(100, -28);
		txtHint.setLayoutData(fd_txtHint);
		
		hintEd = new TibetanEditor(this, SWT.NONE);
		FormData fd_hintEd = new FormData();
		fd_hintEd.bottom = new FormAttachment(txtPos, -6);
		fd_hintEd.top = new FormAttachment(txtRef, 6);
		fd_hintEd.right = new FormAttachment(txtRef, 0, SWT.RIGHT);
		fd_hintEd.left = new FormAttachment(0, 10);
		hintEd.setLayoutData(fd_hintEd);
		
		Button btnSwap = new Button(this, SWT.NONE);
		btnSwap.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String tmp = txtRef.getText();
				txtRef.setText(hintEd.getWorkingText());
				hintEd.setReferenceText(hintEd.getWorkingText());
				hintEd.setWorkingText(tmp);
			}
		});
		FormData fd_btnSwap = new FormData();
		fd_btnSwap.top = new FormAttachment(btnCopy, 0, SWT.TOP);
		fd_btnSwap.left = new FormAttachment(btnCopy, 6);
		btnSwap.setLayoutData(fd_btnSwap);
		btnSwap.setText("Swap");
		
		btnDump = new Button(this, SWT.NONE);
		btnDump.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				hintEd.getDocument().dumpText();
			}
		});
		FormData fd_btnDump = new FormData();
		fd_btnDump.bottom = new FormAttachment(btnCopy, 0, SWT.BOTTOM);
		fd_btnDump.left = new FormAttachment(btnSwap, 6);
		btnDump.setLayoutData(fd_btnDump);
		btnDump.setText("Dump");
		
		Debug debug = new Debug(this, SWT.NONE);
		FormData fd_debug = new FormData();
		fd_debug.right = new FormAttachment(txtRef, 0, SWT.RIGHT);
		fd_debug.bottom = new FormAttachment(btnCopy, -6);
		fd_debug.top = new FormAttachment(txtPos, 6);
		fd_debug.left = new FormAttachment(0, 10);
		debug.setLayoutData(fd_debug);
	
		
	}
	
	public void init() {
		hintEd.init(null);
//		hintEd.init(new TibetanTranscoder());
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	
}
