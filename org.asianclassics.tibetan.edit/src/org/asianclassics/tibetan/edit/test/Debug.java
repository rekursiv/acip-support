package org.asianclassics.tibetan.edit.test;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class Debug extends Composite {
	private Text txtLog;
	private StringBuilder logMsg = new StringBuilder();
	
	static public Debug instance = null;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public Debug(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		txtLog = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
		txtLog.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		instance = this;
	}
	
	public static void log(String msg) {
		if (instance!=null)	instance.logMsg.append(msg);
	}
	
	public static void logLine(String msg) {
		log(msg+"\n");
	}
	
	public static void clearDebug() {
		if (instance!=null)	instance.logMsg.setLength(0);
	}
	
	public static void displayDebug() {
		if (instance!=null)	instance.txtLog.setText(instance.logMsg.toString());
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
