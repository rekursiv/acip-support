package org.asianclassics.tibetan.edit;

import java.util.Arrays;

import org.asianclassics.tibetan.transcoder.TibetanTranscoder;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.widgets.Display;

public class EditManager implements CaretListener {

	public static final int LEFT = -1;
	public static final int RIGHT = 1;
	
	private TibetanPartitionScanner scanner = null;
	private SourceViewer textViewer = null;
	private TibetanTranscoder xcdr = null;
	private boolean doingFormat = false;
	private EditRange editRange = new EditRange();
	private Position caretDocPos = new Position(0);
	private int caretPos = 0;
	private int numUpdateRequests = 0;

	
	public EditManager(TibetanPartitionScanner scanner, SourceViewer textViewer, TibetanTranscoder xcdr) {
		this.scanner = scanner;
		this.textViewer = textViewer;
		this.xcdr = xcdr;
		
		IDocument doc = textViewer.getDocument();
		try {
			doc.addPosition(caretDocPos);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		textViewer.getTextWidget().addCaretListener(this);
		
	}

	
	@Override
	public void caretMoved(CaretEvent event) {
		caretPos = event.caretOffset;
		scheduleUpdate();
	}
	
	public void scheduleUpdate() {
		if (!doingFormat) {
//			System.out.println("OUT: "+caretPos);
			numUpdateRequests++;
			Display.getDefault().timerExec(100, new Runnable() {
				final int id = numUpdateRequests;
				@Override
				public void run() {
					if (id==numUpdateRequests) {
						doingFormat = true;
						update();
						numUpdateRequests=0;
						doingFormat = false;
					}
				}
			});
		} 
//		else {
//			System.out.println("IN : "+caretPos);
//		}
	}
	
	public void test(int arg) {
		if (arg==1)	System.out.println("cp="+caretPos);
		else if (arg==3) scheduleUpdate();
	}
	
	
	public void debugCaretDocPos() {
		IDocument doc = textViewer.getDocument();
		try {
			Position [] ps = doc.getPositions(IDocument.DEFAULT_CATEGORY);
			System.out.println(Arrays.toString(ps));
		} catch (BadPositionCategoryException e) {
			e.printStackTrace();
		}
	}

	public int getGroupType(char ch) {
		if (xcdr.belongsToSyllable(ch)) return TibetanTranscoder.SYLLABLE;
		else return TibetanTranscoder.SEPARATOR;
	}
	
	public boolean isComment(int pos, IDocument doc) {
		try {
			if (doc.getContentType(pos).equals("comment")) return true;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return false;	
	}
	
	public int scanForBoundary(final int startFrom, final int step) {
//		System.out.println(startFrom+"|"+step);
		if (step==0) return 0;
		IDocument doc = textViewer.getDocument();
		
		int pos = startFrom;
		int groupType=0;
		char ch;
		
		if (step<0) {  // join group on the left if starting pt is between
			if (pos==0) return 0;
			pos--;
			if (isComment(pos, doc)) return startFrom;
			try {
				ch = doc.getChar(pos);
				if (ch=='\n') {
//					if (dosLineDelimiters) pos--;
					return pos;
				}
			} catch (BadLocationException e) {
				return 0;
			}
			groupType=getGroupType(ch);
		} else {
			if (isComment(pos, doc)) return startFrom;
			try {
				ch = doc.getChar(pos);
				if (ch=='\n') return startFrom;
				groupType=getGroupType(ch);
			} catch (BadLocationException e) {
				if (step>0) return startFrom;
			}
		}

		do {
			pos+=step;
			try {
				ch = doc.getChar(pos);
			} catch (BadLocationException e) {
				break;
			}
		} while (pos>0&&pos<=doc.getLength()&&getGroupType(ch)==groupType&&ch!='\n');
		
		if (step<0&&pos>0) {
			pos++;
		}
		if (pos<0) pos=0;
		
		return pos;
	}
	
	
	public void update() {
		ISelection selection = textViewer.getSelection();
		if (selection instanceof ITextSelection) {
			ITextSelection textSelection = (ITextSelection)selection;
			if (textSelection.getLength()==0) {
				if (calcEditPos()) formatText();
			}
		}		
	}
	
	public boolean calcEditPos() {
		caretDocPos.setOffset(caretPos);  
		EditRange newEditRange = new EditRange();
		newEditRange.setBegin(scanForBoundary(caretDocPos.getOffset(), LEFT));
		newEditRange.setEnd(scanForBoundary(caretDocPos.getOffset(), RIGHT));
//		System.out.println(newEditRange.debugStartEnd());                            ////////////
		if (!editRange.equals(newEditRange)) {
			editRange = newEditRange;
			scanner.setEditRange(newEditRange);
			return true;
		} else {
			return false;
		}
	}
	
	
	public void formatText() {
		if (!textViewer.canDoOperation(ISourceViewer.FORMAT)) return;
//		System.out.println("-------  begin format   --------");
		
		textViewer.doOperation(ISourceViewer.FORMAT);
//		caretDocPos.setOffset(caretPos);  ///////////////
		textViewer.getTextWidget().setCaretOffset(caretDocPos.getOffset());
		
		if (calcEditPos()) textViewer.doOperation(ISourceViewer.FORMAT);   //  pass #2
		
		//////
//		calcEditPos();
//		textViewer.doOperation(ISourceViewer.FORMAT);
		///////
		
//		System.out.println("^^^^^^^  end format  ^^^^^^^^");
	}




}
