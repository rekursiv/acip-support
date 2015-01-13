package org.asianclassics.text.edit;

import java.awt.Frame;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.google.common.eventbus.EventBus;


public class AcipEditor extends Composite {

	
	private RSyntaxTextArea textArea;
	private RTextScrollPane scrollPane;
	private PlainDocument refDoc;
	private DiffSyntaxDocument wrkDoc;
	private EventBus eb;
	private Point prevScrollPos = new Point();

	public enum ScrollMode { DEFAULT, CENTER, CENTER_ON_SPACE };
	private ScrollMode scrollMode = ScrollMode.CENTER;


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
		
		textArea.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent evt) {
				postCaretMoveEventAsync();
			}
		});
		
		scrollPane = new RTextScrollPane(textArea);
		scrollPane.getViewport().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				postScrollEventAsync();
			}
		});
    
	    applet.add(scrollPane);
	}

	
	
	protected void postCaretMoveEventAsync() {
		if (scrollMode!=ScrollMode.DEFAULT) {
			Point caretPixelPos = textArea.getCaret().getMagicCaretPosition();
			if (caretPixelPos!=null) {
				try {
					int leftOfCaretPos = textArea.getCaretPosition()-1;
					if (leftOfCaretPos>=0) {
						if (scrollMode==ScrollMode.CENTER || wrkDoc.getText(textArea.getCaretPosition()-1, 1).equals(" ")) {
							Point scrollPos = scrollPane.getViewport().getViewPosition();
							int vpWidth = textArea.getVisibleRect().width;
							scrollPos.x = caretPixelPos.x - (vpWidth / 2);
							if (scrollPos.x<0) scrollPos.x=0;
							int maxScrollX = textArea.getWidth()-vpWidth;
							if (scrollPos.x>maxScrollX) scrollPos.x = maxScrollX;
							if (scrollPos.x!=prevScrollPos.x || scrollPos.y!=prevScrollPos.y) {
//								System.out.println("# "+scrollPos.x);
								scrollPane.getViewport().setViewPosition(scrollPos);
							}
						}
					}
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (eb!=null) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					eb.post(new AcipEditorCaretMoveEvent(textArea.getCaretLineNumber(), textArea.getCaretOffsetFromLineStart()));
				}
			});
		}
	}

	public void setEventBus(EventBus eb) {
		this.eb = eb;
	}

	protected void postScrollEventAsync() {
		if (eb!=null) {
			Point pos = scrollPane.getViewport().getViewPosition();
			if (pos.x!=prevScrollPos.x || pos.y!=prevScrollPos.y) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
//						System.out.println("* "+pos);
						eb.post(new AcipEditorScrollEvent(pos.x, pos.y));
					}
				});
				prevScrollPos = pos;
			}
		}
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
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
