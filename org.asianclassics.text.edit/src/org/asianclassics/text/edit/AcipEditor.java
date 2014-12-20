package org.asianclassics.text.edit;

import java.awt.Frame;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;


public class AcipEditor extends Composite {

	
	private RSyntaxTextArea textArea;
	private PlainDocument refDoc;
	private DiffSyntaxDocument wrkDoc;

	public AcipEditor(Composite parent) {
		super(parent, SWT.EMBEDDED|SWT.NO_BACKGROUND);
		System.setProperty("sun.awt.noerasebackground", "true");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	    Frame frame = SWT_AWT.new_Frame(this);
	    JApplet applet = new JApplet();
	    frame.add(applet);
	     
		refDoc = new PlainDocument();
		DiffTokenMaker dtm = new DiffTokenMaker(refDoc);
		wrkDoc = new DiffSyntaxDocument(dtm);
		
		textArea = new RSyntaxTextArea(wrkDoc);
		textArea.addParser(new SpellCheckParser());
		textArea.setParserDelay(500);
		
		loadTheme("diff-theme");
		
		RTextScrollPane sp = new RTextScrollPane(textArea);
    
	    applet.add(sp);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void setWorkingText(String text) {
//		System.out.println("setWorkingText:  "+text );

		try {
			wrkDoc.remove(0, wrkDoc.getLength());
			wrkDoc.insertString(0, text, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	}

	public String getWorkingText() {
		try {
			return wrkDoc.getText(0, wrkDoc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setReferenceText(String text) {
//		System.out.println("setReferenceText:  "+text );

		try {
			refDoc.remove(0, refDoc.getLength());
			refDoc.insertString(0, text, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getReferenceText() {
		try {
			return refDoc.getText(0, refDoc.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return null;
		}
	}


	void loadTheme(String themeName) {
		InputStream is = getClass().getResourceAsStream("/"+themeName+".xml");
		if (is==null) {
//			throw new Exception("Resource file not found.");
			System.err.println("Resource file not found.");
			return;
		}
		try {
			Theme theme = Theme.load(is);
			theme.apply(textArea);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
