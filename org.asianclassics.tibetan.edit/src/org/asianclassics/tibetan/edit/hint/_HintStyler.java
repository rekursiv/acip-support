package org.asianclassics.tibetan.edit.hint;

import org.asianclassics.tibetan.edit.hint.HintRange.Hint;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;


public class _HintStyler implements IPartitionTokenScanner {

	private int tokenLength;
	private int tokenOffset;
	private DiffDocument document;
	private int rangeEnd;
	private int curOffset;
	private int tokenCount;

	@Override
	public void setRange(IDocument document, int offset, int length) {
		this.document = (DiffDocument) document;
		rangeEnd = offset+length;
		curOffset = offset;
		tokenOffset = offset;
		tokenLength = 0;
		tokenCount=0;

		this.document.diff();
		
//		System.out.println("* setRange:   offset="+offset+"   len="+length);
	}
	
	@Override
	public void setPartialRange(IDocument document, int offset, int length, String contentType, int partitionOffset) {
//		System.out.println("* setPartialRange:   offset="+offset+"   len="+length+"   ct="+contentType+"   po="+partitionOffset);
		setRange(document, offset, length);
	}
	
	
	

	@Override
	public IToken nextToken() {
//		System.out.println("* nextToken  to:"+tokenOffset+"   co:"+curOffset);
		if (!document.hasReference()) return Token.EOF;
		
		tokenCount++;
		if (tokenCount > 1000) {
			System.out.println("HintStyler detected possible infinite loop!");
			return Token.EOF;
		}
		
		tokenOffset = curOffset;
		HintRange hintRange = document.getHintRange(curOffset);
		curOffset = hintRange.getEnd();
//		if (curOffset>rangeEnd) curOffset = rangeEnd;
		return buildToken(hintRange);
	}

	
	
	private IToken buildToken(HintRange hintRange) {
		tokenLength = curOffset-tokenOffset;
//		System.out.println("* "+tokenOffset+":"+curOffset+":"+hintRange.getType());
		
		if (hintRange.getType()==Hint.EOF) {
//			System.out.println("* EOF\n\n");
			return Token.EOF;
//		} else if (tokenLength<=0||curOffset>rangeEnd) {
		} else if (tokenLength<=0) {
			System.out.println("* ERROR: returning EOF  tl="+tokenLength+"  co="+curOffset+"   re="+rangeEnd);
			return Token.EOF;
		}
		else if (hintRange.getType()==Hint.EXTRA) return new Token(getStyle(180, 255, 180));
		else if (hintRange.getType()==Hint.DIFFERENT) return new Token(getStyle(255, 180, 180));
		else if (hintRange.getType()==Hint.MISSING) return new Token(getStyle(180, 180, 255));
		else return new Token(null);
	}
	
	
	public TextAttribute getStyle(int bgRed, int bgGreen, int bgBlue) {
//		return new TextAttribute(SWTResourceManager.getColor(0, 0, 0), SWTResourceManager.getColor(bgRed, bgGreen, bgBlue), SWT.NORMAL);
		return new TextAttribute(SWTResourceManager.getColor(0, 0, 0), SWTResourceManager.getColor(bgRed, bgGreen, bgBlue), SWT.NORMAL, SWTResourceManager.getFont("TibetanMachineUni", 26, SWT.NORMAL));
	}

	@Override
	public int getTokenOffset() {
		return tokenOffset;
	}

	@Override
	public int getTokenLength() {
		return tokenLength;
	}
	

}
