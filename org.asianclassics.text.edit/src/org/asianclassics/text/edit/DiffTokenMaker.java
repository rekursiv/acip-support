package org.asianclassics.text.edit;

import java.util.LinkedList;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;

import org.asianclassics.text.edit.diff_match_patch.Diff;
import org.asianclassics.text.edit.diff_match_patch.Operation;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerBase;
import org.fife.ui.rsyntaxtextarea.TokenMap;




public class DiffTokenMaker extends TokenMakerBase {

	
	public enum Hint {
		OK, MISSING, DIFFERENT, EXTRA, MARK, EOF
	}
	
	diff_match_patch dmp = new diff_match_patch();

	
	private int curTokenStartPos = 0;
	private int curStartOffset = 0;
	private Hint curTokenHint = Hint.OK;
	private Segment curLineSegment = null;

	private PlainDocument refDoc = null;
	
	
	public DiffTokenMaker() {
//		System.out.println("***");
	}

	
/*

different:  VARIABLE	:	DELETE*|INSERT	: red bg
extra:      REGEX		:	INSERT*|EQUAL	: orange bg
missing:    SEPARATOR	:	DELETE*|EQUAL	: green bg  (both sides)
	

=== DIFFERENT
Diff(EQUAL,"the d")
Diff(DELETE,"o")
Diff(INSERT,"0")
Diff(EQUAL,"ors of perception")

=== EXTRA
Diff(EQUAL,"the doors of per")
Diff(INSERT,"x")
Diff(EQUAL,"ception")

=== MISSING
Diff(EQUAL,"the do")
Diff(DELETE,"o")
Diff(EQUAL,"rs of perception")

*/
	
	
	public DiffTokenMaker(PlainDocument refDoc) {
		super();
		this.refDoc = refDoc;
	}
	
	public String getLine(int lineNum) {
		if (lineNum<0) return "";
		Element root = refDoc.getDefaultRootElement();
		if (lineNum>=root.getElementCount()) return "";
		Element line = root.getElement(lineNum);
		try {
			return refDoc.getText(line.getStartOffset(), line.getEndOffset()-line.getStartOffset()-1);
		} catch (BadLocationException e) {
			e.printStackTrace();
			return "";
		}
	}


	public Token getTokenList(Segment lineSegment, int initialTokenType, int startOffset, int lineNum) {

		curTokenStartPos = 0;
		curStartOffset = startOffset;
		curTokenHint = Hint.OK;
		curLineSegment = lineSegment;

		System.out.println(lineNum+" '"+curLineSegment+"' "+startOffset+":"+curLineSegment.offset);
		
		resetTokenList();
		
		if (refDoc==null || refDoc.getLength()==0) {
			addToken(lineSegment.count-1, "null");
			addNullToken();
			return firstToken;
		}
		
		String refStr = getLine(lineNum);
		
		
		
		LinkedList<Diff> diffs = dmp.diff_main(refStr, curLineSegment.toString());
		dmp.diff_cleanupSemantic(diffs);

		int endComp = 1;
		int missingComp = 1;
		Diff diff = null;
		Diff equalDiff = null;
		Diff deleteDiff = null;
		Diff insertDiff = null;
		Operation curOperation = null;
		
		for (int i=0; i<=diffs.size(); ++i) {
			if (i<diffs.size()) {
				diff = diffs.get(i);
				curOperation = diff.operation;
			}
			else {
				diff = null;
				curOperation = Operation.EQUAL;
			}
		
			if (curOperation==Operation.EQUAL) {
				if (deleteDiff!=null&&insertDiff!=null) {		// different
					if (equalDiff!=null) {
						curTokenHint = Hint.OK;
						addToken(curTokenStartPos+equalDiff.text.length()-endComp, "<df");
						curTokenStartPos=curTokenStartPos+equalDiff.text.length()-(endComp-1);
						endComp=1;
					}
					curTokenHint = Hint.DIFFERENT;
					addToken(curTokenStartPos+insertDiff.text.length()-1, "df");
					curTokenStartPos=curTokenStartPos+insertDiff.text.length();
				} else if (deleteDiff!=null) {					// missing
					if (curTokenStartPos==0) endComp=2;
					else endComp=2;
					if (equalDiff!=null) {
						curTokenHint = Hint.OK;
						addToken(curTokenStartPos+equalDiff.text.length()-endComp, "<ms");
						curTokenStartPos=curTokenStartPos+equalDiff.text.length()-(endComp-1);
						if (diff==null) missingComp=0;
						else missingComp=1;
					} else {
						missingComp=0;
					}
					curTokenHint = Hint.MISSING;
					addToken(curTokenStartPos+missingComp, "ms");
					curTokenStartPos=curTokenStartPos+missingComp+1;
				} else if (insertDiff!=null) {					// extra
					if (equalDiff!=null) {
						curTokenHint = Hint.OK;
						addToken(curTokenStartPos+equalDiff.text.length()-endComp, "<xt");
						curTokenStartPos=curTokenStartPos+equalDiff.text.length()-(endComp-1);
						endComp=1;
					}
					curTokenHint = Hint.EXTRA;
					addToken(curTokenStartPos+insertDiff.text.length()-1, "xt");
					curTokenStartPos=curTokenStartPos+insertDiff.text.length();
				}
				deleteDiff = null;
				insertDiff = null;
				equalDiff = diff;
			} else if (curOperation==Operation.DELETE) {
//				System.err.println("***");
				deleteDiff = diff;
			} else if (curOperation==Operation.INSERT) {
				insertDiff = diff;
			}
			System.out.println(curTokenStartPos+":"+diff);
		}
		
		curTokenHint = Hint.OK;
		addToken(lineSegment.count-1, "fn");

		
//		System.out.println("===========");
		addNullToken();
		return firstToken;
	}
	
	
	public void addToken(int end, String debugTag) {
		if (curTokenStartPos<0||end<0||curTokenStartPos>end||end>=curLineSegment.count) {
			System.out.println(">>>  "+debugTag+":"+curTokenStartPos+":"+end+":"+curLineSegment.count);
			return;
		}
		
		int tokenType = Token.IDENTIFIER;
		if (curTokenHint==Hint.DIFFERENT) tokenType = Token.VARIABLE;
		else if (curTokenHint==Hint.MISSING) tokenType = Token.SEPARATOR;
		else if (curTokenHint==Hint.EXTRA) tokenType = Token.REGEX;
	
		System.out.println("  ["+debugTag+":"+curTokenStartPos+":"+end+"]");
//		debugToken(end, debugTag);

		addToken(curLineSegment, curTokenStartPos+curLineSegment.offset, end+curLineSegment.offset, tokenType, curTokenStartPos+curStartOffset);
	}

	
	public void debugToken(int end, String debugTag) {
		String s = "";
		try {
			s = curLineSegment.toString().substring(curTokenStartPos, end+1);
		} catch (StringIndexOutOfBoundsException e) {
			System.err.print("#");
			System.out.println("#  "+debugTag+":"+curTokenStartPos+":"+end+":"+curLineSegment.count);
			return;
		}
		System.out.println("  ["+debugTag+":"+curTokenStartPos+":"+end+":'"+s+"']");
	}
	
	
	
	
	public void _addToken(Segment segment, int start, int end, Hint hint, int startOffset, String debugTag) {
		if (start<0||end<0||start>end||end>=segment.count) {
			System.out.println(">>>  "+debugTag+":"+start+":"+end+":"+segment.count);
			return;
		}
		
		int tokenType = Token.IDENTIFIER;
		if (hint==Hint.DIFFERENT) tokenType = Token.VARIABLE;
		else if (hint==Hint.MISSING) tokenType = Token.SEPARATOR;
		else if (hint==Hint.EXTRA) tokenType = Token.REGEX;
		
		
		
		String s = "";
		try {
			s = segment.toString().substring(start, end+1);
		} catch (StringIndexOutOfBoundsException e) {
			System.err.print("#");
			System.out.println("#  "+debugTag+":"+start+":"+end+":"+segment.count);
			return;
		}
		System.out.println("  ["+debugTag+":"+start+":"+end+":'"+s+"']");
	
	
		
		addToken(segment, start+segment.offset, end+segment.offset, tokenType, start+startOffset);
	}

	
	
	@Override
	public int getLastTokenTypeOnLine(Segment text, int initialTokenType) {
//		System.err.println("****");
//		return super.getLastTokenTypeOnLine(text, initialTokenType);
		return Token.NULL;
	}


	@Override
	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
		return null;
	}
	
	
}



