package org.asianclassics.tibetan.edit.hint;

import org.asianclassics.tibetan.edit.hint.HintRange.Hint;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.wb.swt.SWTResourceManager;

public class TibetanHintStyler implements IPartitionTokenScanner {

	private int tokenLength;
	private int tokenOffset;
	private DiffDocument document;
	private int rangeEnd;
	private int curOffset;
	private int tokenCount;
	private String contentType;
	private boolean tokenEof;
	
	
	public TibetanHintStyler(String contentType) {
		this.contentType = contentType;
	}
	
	
	@Override
	public void setRange(IDocument document, int offset, int length) {
		this.document = (DiffDocument) document;
		rangeEnd = offset+length;
		curOffset = offset;
		tokenOffset = offset;
		tokenLength = length;
		tokenCount=0;
		tokenEof = false;

		this.document.diff();
		
//		System.out.println("* setRange:   offset="+offset+"   len="+length+"    ct="+contentType);
	}
	
	@Override
	public void setPartialRange(IDocument document, int offset, int length, String contentType, int partitionOffset) {
//		System.out.println("* setPartialRange:   offset="+offset+"   len="+length+"   ct="+contentType+"   po="+partitionOffset);
		setRange(document, offset, length);
	}
	
	

	@Override
	public IToken nextToken() {
//		System.out.println("* nextToken  to:"+tokenOffset+"   co:"+curOffset);
		if (!document.hasReference()) {
			if (tokenEof) return Token.EOF;
			tokenEof = true;
			return new Token(getStyle(Hint.OK));
		}
		
		if (curOffset==rangeEnd) return Token.EOF;
		
		tokenCount++;
		if (tokenCount > 1000) {
			System.out.println("HintStyler detected possible infinite loop!");
			return Token.EOF;
		}
		
		tokenOffset = curOffset;
		HintRange hintRange = document.getHintRange(curOffset);
		curOffset = hintRange.getEnd();
		
		if (curOffset>rangeEnd) curOffset = rangeEnd;
		
		return buildToken(hintRange);
	}
	
	private IToken buildToken(HintRange hintRange) {
		tokenLength = curOffset-tokenOffset;
//		System.out.println("* "+tokenOffset+":"+curOffset+":"+hintRange.getType());
		
		if (hintRange.getType()==Hint.EOF) {
//			System.out.println("* EOF\n\n");
			return Token.EOF;
		} else if (tokenLength<=0) {
			System.out.println("* ERROR: returning EOF  tl="+tokenLength+"  co="+curOffset+"   re="+rangeEnd);
			return Token.EOF;
		} 
		else return new Token(getStyle(hintRange.getType()));
	}
	
	private TextAttribute getStyle(Hint hint) {
		boolean isTibetan = false;
		if (contentType.startsWith("tibetan")) isTibetan = true;
		if (contentType.endsWith("edit")) {
			return getStyle(255, 255, 220, isTibetan);
		} else {
			if (hint==Hint.EXTRA) return getStyle(180, 255, 180, isTibetan);
			else if (hint==Hint.DIFFERENT) return getStyle(255, 180, 180, isTibetan);
			else if (hint==Hint.MISSING) return getStyle(180, 180, 255, isTibetan);
			else return getStyle(255, 255, 255, isTibetan);
		}
	}
	
	private TextAttribute getStyle(int bgRed, int bgGreen, int bgBlue, boolean isTibetan) {
		Font font;
		if (isTibetan) font = SWTResourceManager.getFont("TibetanMachineUni", 26, SWT.NORMAL);
		else font = JFaceResources.getBannerFont();
		return new TextAttribute(
				SWTResourceManager.getColor(0, 0, 0),
				SWTResourceManager.getColor(bgRed, bgGreen, bgBlue),
				SWT.NORMAL,
				font);
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
