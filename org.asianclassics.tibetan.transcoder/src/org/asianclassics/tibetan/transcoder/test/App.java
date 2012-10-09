package org.asianclassics.tibetan.transcoder.test;

import org.asianclassics.tibetan.transcoder.TibetanTranscoder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class App {

	
	public static final String startingText = "#, ,MDZAD PA GANG, ,GANG DANG YANG DAG LDAN, ,RNAM PA THAMS CAD";

//	public static final String startingText = "PA KA ,SUM";
	
	public static App instance;
	
	private TibetanTranscoder xcdr = new TibetanTranscoder();
	protected Shell shell;
	
	public Text txtTop;
	private Text txtMid;
	private Text txtBtm;
	private Button btnXcodeMid;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			App window = new App();
			instance = window;
			window.open();
//			window.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		SWTResourceManager.dispose();
	}
	
	private void doXcodeTop() {
		String top = txtTop.getText();
		
		String mid = xcdr.transcode(top, TibetanTranscoder.ROMAN);
		txtMid.setText(mid);
		
	}
	
	private void doXcodeMid() {
		String mid = txtMid.getText();
		
		String btm = xcdr.transcode(mid, TibetanTranscoder.TIBETAN);
		txtBtm.setText(btm);
	}
	
	private void init() {
		txtTop.setText(startingText);
	
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {		
		shell = new Shell();
		shell.setSize(671, 593);
		shell.setText("Tibetan Transcoder Test");
		shell.setLayout(new FormLayout());
		
		Button btnXcode = new Button(shell, SWT.NONE);
		btnXcode.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doXcodeTop();
			}
		});
		FormData fd_btnXcode = new FormData();
		fd_btnXcode.top = new FormAttachment(0, 5);
		fd_btnXcode.left = new FormAttachment(0, 5);
		btnXcode.setLayoutData(fd_btnXcode);
		btnXcode.setText("xcode top");
		
		txtTop = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(btnXcode, 6);
		fd_text.right = new FormAttachment(100, -10);
		fd_text.left = new FormAttachment(0, 5);
		txtTop.setLayoutData(fd_text);
		txtTop.setFont(SWTResourceManager.getFont("TibetanMachineUni", 20, SWT.NORMAL));
		
		txtMid = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		fd_text.bottom = new FormAttachment(100, -369);
		FormData fd_text_1 = new FormData();
		fd_text_1.right = new FormAttachment(txtTop, 0, SWT.RIGHT);
		fd_text_1.top = new FormAttachment(txtTop, 6);
		fd_text_1.left = new FormAttachment(btnXcode, 0, SWT.LEFT);
		txtMid.setLayoutData(fd_text_1);
		txtMid.setFont(SWTResourceManager.getFont("TibetanMachineUni", 20, SWT.NORMAL));
		
		txtBtm = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		fd_text_1.bottom = new FormAttachment(100, -213);
		FormData fd_text_2 = new FormData();
		fd_text_2.bottom = new FormAttachment(100, -57);
		fd_text_2.top = new FormAttachment(txtMid, 6);
		fd_text_2.left = new FormAttachment(btnXcode, 0, SWT.LEFT);
		fd_text_2.right = new FormAttachment(txtTop, 0, SWT.RIGHT);
		txtBtm.setLayoutData(fd_text_2);
		txtBtm.setFont(SWTResourceManager.getFont("TibetanMachineUni", 20, SWT.NORMAL));
		
		btnXcodeMid = new Button(shell, SWT.NONE);
		btnXcodeMid.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doXcodeMid();
			}
		});
		btnXcodeMid.setText("xcode mid");
		FormData fd_btnXcodeMid = new FormData();
		fd_btnXcodeMid.bottom = new FormAttachment(btnXcode, 0, SWT.BOTTOM);
		fd_btnXcodeMid.left = new FormAttachment(btnXcode, 10);
		btnXcodeMid.setLayoutData(fd_btnXcodeMid);
		
		init();

	}
}
