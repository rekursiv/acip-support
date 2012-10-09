package org.asianclassics.tibetan.edit;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class AttributeScanner implements IPartitionTokenScanner {

	private int tokenLength;
	private int tokenOffset;
	private boolean done = false;
	
	private TextAttribute ta = null;
	
	public AttributeScanner(TextAttribute ta) {
		this.ta = ta;
	}

	@Override
	public void setRange(IDocument document, int offset, int length) {
		tokenOffset = offset;
		tokenLength = length;
		done = false;
//		System.out.println("* setRange:   offset="+offset+"   len="+length);
	}

	@Override
	public IToken nextToken() {
//		System.out.println("* nextToken"); 
		if (done || ta == null) return Token.EOF;
		done = true;
		return new Token(ta);
	}

	@Override
	public int getTokenOffset() {
//		System.out.println("* getTokenOffset "+tokenOffset); 
		return tokenOffset;
	}

	@Override
	public int getTokenLength() {
//		System.out.println("* getTokenLength "+tokenLength); 
		return tokenLength;
	}

	@Override
	public void setPartialRange(IDocument document, int offset, int length, String contentType, int partitionOffset) {
//		System.out.println("* setPartialRange:   offset="+offset+"   len="+length+"   ct="+contentType+"   po="+partitionOffset);
		setRange(document, offset, length);
	}

}
