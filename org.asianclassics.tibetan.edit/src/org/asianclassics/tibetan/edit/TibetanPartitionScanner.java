package org.asianclassics.tibetan.edit;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class TibetanPartitionScanner implements IPartitionTokenScanner {

	private IDocument document;
	private int rangeEnd;
	private int curOffset;
	private int tokenLength;
	private int tokenOffset;
	
	private EditRange editRange = new EditRange();
	
	boolean inComment = false;
	
//	private int index; //

	
	public void setEditRange(EditRange editRange) {
		this.editRange = editRange;
	}
	
	@Override
	public void setRange(IDocument document, int offset, int length) {
		this.document = document;
		rangeEnd = offset+length;
		curOffset = offset;
		tokenOffset = offset;
		tokenLength = 0;
		inComment = false;
//		index = -1;  //
		System.out.println("# setRange:   offset="+offset+"   len="+length);
	}
	

	@Override
	public void setPartialRange(IDocument document, int offset, int length, String contentType, int partitionOffset) {
//		System.out.println("# setPartialRange:   offset="+offset+"   len="+length+"   ct="+contentType+"   po="+partitionOffset);
		
		setRange(document, offset, length);     //  FIXME
	}



	@Override
	public IToken nextToken() {
//		index++;
		char ch = 0;
		char prevCh = 0;
		tokenOffset = curOffset;
		boolean enteredTibetan = false;
		boolean enteredRoman = false;
		boolean inEditWindow = false;
//		boolean inComment = false;
		int editRangeBegin = editRange.getBegin();
		int editRangeEnd = editRange.getEnd();
		while (curOffset<rangeEnd) {
			try {
				ch = document.getChar(curOffset);
			} catch (BadLocationException e) {
			}

			if (ch=='\n') {
				if (curOffset>tokenOffset) {
					return buildToken(enteredTibetan, inEditWindow, inComment);
				} else {
//					System.out.print("f");
					++curOffset;
					tokenLength = 1;
					return new Token("ctrl");
				}
			}
			
			if (ch=='[') {
				if (curOffset>tokenOffset) {
					return buildToken(enteredTibetan, inEditWindow, inComment);
				} else {
					inComment = true;
				}
			}
			
			if (ch==']') {
				if (inComment) {
					inComment = false;
					++curOffset;
					return buildToken(enteredTibetan, inEditWindow, true);
				}
				// TODO:  disallow Tibetan inside a comment??  (less confusing when editing...)
			}
			
			if (!inComment) {
				if (curOffset>tokenOffset) {
//					System.out.println("to="+tokenOffset+"  co="+curOffset+"  ere="+editRangeEnd);
					if (curOffset==editRangeBegin) return buildToken(enteredTibetan, false, false);
					else if (curOffset==editRangeEnd) return buildToken(enteredTibetan, true, false);
				}
				if (ch>=0x0F00&&ch<=0x0FFF) {
//					System.out.print("T");
					if (enteredRoman) return buildToken(enteredTibetan, inEditWindow, false);
					enteredTibetan = true;
				} else {
					// space chars are used in both Tibetan and Roman text, so we have to do extra investigation here...
					boolean isRoman = true;
					if (ch==' ') {
						// we check the prev char for shay-like thing
						if (prevCh>=0xF0D&&prevCh<=0xF11) isRoman = false;
						// and we also have to check the next char for shay-like thing
						char nextCh = 0;
						try {
							nextCh = document.getChar(curOffset+1);
						} catch (BadLocationException e) {
						}
						if (nextCh>=0xF0D&&nextCh<=0xF11) isRoman = false;
					}
					if (isRoman) {
//					System.out.print("R");
						if (enteredTibetan) return buildToken(enteredTibetan, inEditWindow, false);
						enteredRoman = true;
					}
				}
				
			}
			
			if (curOffset>=editRangeBegin&&curOffset<editRangeEnd) inEditWindow = true;
			++curOffset;
			prevCh = ch;
		}
		return buildToken(enteredTibetan, inEditWindow, inComment);
	}
	
	private IToken buildToken(boolean isTibetan, boolean inEditWindow, boolean inComment) {
		tokenLength = curOffset-tokenOffset;
		if (tokenLength<=0) return Token.EOF;
//		if (tokenLength<=0) {System.out.println("<");return Token.EOF;}   //  TEST
		else if (inComment) {
			return new Token("comment");
		} else {
			StringBuilder pType = new StringBuilder();
			if (isTibetan) pType.append("tibetan_");
			else pType.append("roman_");
			if (inEditWindow) pType.append("edit");
			else pType.append("display");
//			System.out.println("token << "+pType.toString()+"   offset="+tokenOffset+"   len="+tokenLength);
			return new Token(pType.toString());
		}
	}
	
	@Override
	public int getTokenOffset() {
		return tokenOffset;
	}

	@Override
	public int getTokenLength() {
		return tokenLength;
	}

	public String[] getLegalContentTypes() {
		return new String[] {"tibetan_display", "tibetan_edit", "roman_display", "roman_edit", "ctrl", "comment"};
	}

}
